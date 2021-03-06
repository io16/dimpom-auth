package com.queue.auth.rest;

import com.google.inject.*;
import com.queue.auth.util.Framework;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.CorsHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.vertx.core.http.HttpMethod.POST;

public class Routing extends AbstractModule {
  @Provides @Singleton
  Router router(Injector injector, Framework framework) {
    var router = Router.router(framework.vertx());

    router.route().handler(getCorsHandler());

    for (Route route : getAllRoutes(injector))
      route.configure(router);

    return router;
  }

  private CorsHandler getCorsHandler() {
    var corsHandler = CorsHandler.create("*");
    corsHandler.allowedHeaders(Set.of(
        "x-requested-with",
        "Access-Control-Allow-Origin",
        "origin",
        "Content-Type",
        "accept"
    ));
    corsHandler.getDelegate().allowedMethods(Set.of(POST));
    return corsHandler;
  }

  private List<Route> getAllRoutes(Injector injector) {
    var routes = new ArrayList<Route>();

    for (Key<?> key : injector.getAllBindings().keySet())
      if (Route.class.isAssignableFrom(key.getTypeLiteral().getRawType()))
        routes.add((Route) injector.getInstance(key));

    return routes;
  }
}
