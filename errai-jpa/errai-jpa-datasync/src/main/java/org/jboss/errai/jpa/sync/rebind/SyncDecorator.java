package org.jboss.errai.jpa.sync.rebind;

import static org.jboss.errai.codegen.meta.MetaClassFactory.parameterizedAs;
import static org.jboss.errai.codegen.meta.MetaClassFactory.typeParametersOf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.errai.codegen.Parameter;
import org.jboss.errai.codegen.Statement;
import org.jboss.errai.codegen.builder.AnonymousClassStructureBuilder;
import org.jboss.errai.codegen.builder.BlockBuilder;
import org.jboss.errai.codegen.exception.GenerationException;
import org.jboss.errai.codegen.meta.MetaClass;
import org.jboss.errai.codegen.meta.MetaClassFactory;
import org.jboss.errai.codegen.meta.MetaField;
import org.jboss.errai.codegen.meta.MetaMethod;
import org.jboss.errai.codegen.meta.MetaParameter;
import org.jboss.errai.codegen.util.GenUtil;
import org.jboss.errai.codegen.util.Refs;
import org.jboss.errai.codegen.util.Stmt;
import org.jboss.errai.ioc.client.api.CodeDecorator;
import org.jboss.errai.ioc.client.container.InitializationCallback;
import org.jboss.errai.ioc.rebind.ioc.extension.IOCDecoratorExtension;
import org.jboss.errai.ioc.rebind.ioc.injector.InjectUtil;
import org.jboss.errai.ioc.rebind.ioc.injector.api.InjectableInstance;
import org.jboss.errai.jpa.sync.client.local.ClientSyncWorker;
import org.jboss.errai.jpa.sync.client.local.DataSyncCallback;
import org.jboss.errai.jpa.sync.client.local.Sync;
import org.jboss.errai.jpa.sync.client.local.SyncParam;
import org.jboss.errai.jpa.sync.client.shared.SyncResponses;

/**
 * Generates an {@link InitializationCallback} that contains data sync logic.
 * 
 * @author Christian Sadilek <csadilek@redhat.com>
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 */
@CodeDecorator
public class SyncDecorator extends IOCDecoratorExtension<Sync> {

  public SyncDecorator(Class<Sync> decoratesWith) {
    super(decoratesWith);
  }

  @Override
  public List<? extends Statement> generateDecorator(InjectableInstance<Sync> ctx) {

    MetaMethod method = ctx.getMethod();
    MetaParameter[] params = method.getParameters();
    if (params.length != 1 || !params[0].getType().getErased().equals(MetaClassFactory.get(SyncResponses.class))) {
      throw new GenerationException("Methods annotated with @" + Sync.class.getName()
          + " need to have exactly one parameter of type: "
          + SyncResponses.class.getName() + 
          ". Check method: "  + GenUtil.getMethodString(method) + " in class " + method.getDeclaringClass().getFullyQualifiedName());
    }
    
    final List<Statement> statements = new ArrayList<Statement>();

    Sync syncAnnotation = ctx.getAnnotation();
    statements.add(Stmt.declareFinalVariable("objectClass", Class.class, Stmt.loadLiteral(Object.class)));

    statements.add(Stmt.declareFinalVariable(
            "syncWorker",
            ClientSyncWorker.class,
            Stmt.invokeStatic(ClientSyncWorker.class, "create", syncAnnotation.query(),
                    Stmt.loadVariable("objectClass"), null)));

    statements.add(Stmt.loadVariable("syncWorker").invoke("addSyncCallback", createSyncCallback(ctx)));

    ctx.getTargetInjector().addStatementToEndOfInjector(
        Stmt.loadVariable("context").invoke("addInitializationCallback",
                  Refs.get(ctx.getInjector().getInstanceVarName()),
                  createInitCallback(ctx.getEnclosingType(), "obj", syncAnnotation, ctx)));

    Statement destruction = Stmt.loadVariable("syncWorker").invoke("stop");
    ctx.getTargetInjector().addStatementToEndOfInjector(
            Stmt.loadVariable("context").invoke(
                    "addDestructionCallback",
                    Refs.get(ctx.getInjector().getInstanceVarName()),
                    InjectUtil.createDestructionCallback(ctx.getEnclosingType(), "obj",
                            Collections.singletonList(destruction))));

    return statements;

  }

  /**
   * Generates an anonymous {@link DataSyncCallback} that will invoke the decorated sync method.
   */
  private Statement createSyncCallback(InjectableInstance<Sync> ctx) {
    return Stmt.newObject(DataSyncCallback.class)
            .extend()
            .publicOverridesMethod("onSync", Parameter.of(SyncResponses.class, "responses", true))
            .append(ctx.callOrBind(Stmt.loadVariable("responses")))
            .finish()
            .finish();
  }

  /**
   * Generates an anonymous {@link InitializationCallback} that will contain the logic to start the
   * {@link ClientSyncWorker}.
   */
  private Statement createInitCallback(final MetaClass type, final String initVar, final Sync syncAnnotation,
      final InjectableInstance<Sync> ctx) {

    BlockBuilder<AnonymousClassStructureBuilder> method =
        Stmt.newObject(parameterizedAs(InitializationCallback.class, typeParametersOf(type)))
            .extend()
            .publicOverridesMethod("init", Parameter.of(type, initVar, true));

    method.append(Stmt.declareFinalVariable("paramsMap", Map.class, Stmt.newObject(HashMap.class)));

    for (SyncParam param : syncAnnotation.params()) {
      Statement fieldValueStmt;
      String val = param.val().trim();
      if (val.startsWith("{") && val.endsWith("}")) {
        String fieldName = val.substring(1, val.length() - 1);
        MetaField field = ctx.getEnclosingType().getField(fieldName);
        fieldValueStmt =
            InjectUtil.getPublicOrPrivateFieldValue(ctx.getInjectionContext(), Stmt.loadVariable(ctx.getInjector()
                .getInstanceVarName()), field);
      }
      else {
        fieldValueStmt = Stmt.loadLiteral(val);
      }
      method.append(Stmt.loadVariable("paramsMap").invoke("put", param.name(), fieldValueStmt));
    }

    return method
        .append(Stmt.loadVariable("syncWorker").invoke("start", Stmt.loadVariable("paramsMap")))
        .finish()
        .finish();
  }

}
