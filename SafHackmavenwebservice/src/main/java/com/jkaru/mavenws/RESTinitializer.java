/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkaru.mavenws;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import process.ProcessMessage;

/**
 *
 * @author jkaru
 */
public class RESTinitializer  {
    static ProcessMessage proc = new ProcessMessage();
    public static void main(String[] args) {
       Vertx vertx = Vertx.vertx();
       
       HttpServer httpserver = vertx.createHttpServer();
       
       
       Router router = Router.router(vertx);
       
       Route handler = router
               .route("/hello/:request")
               .handler(routingContext -> {
                   
                   String request = routingContext.request().getParam("request");
                HttpServerResponse response = routingContext.response();
                response.write("\nYou sent idnumber : \n" + request);
                proc.Process2(request);
                routingContext
                        .vertx()
                        .setTimer(5000,tid -> routingContext.next());              
               });
       
       
       
       
       httpserver.requestHandler(router::accept)
               .listen(8094);
    }
    
}
