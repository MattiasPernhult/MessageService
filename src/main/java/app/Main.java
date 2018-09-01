package app;

import app.db.MemoryDB;
import app.db.Type;
import app.service.jwt.Jwt;
import app.service.jwt.JwtHS256;
import app.service.logger.StandardOutputLogger;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        MemoryDB db = new MemoryDB();

        String jwtIssuer = dotenv.get("JWT_ISSUER");
        String jwtSecret = dotenv.get("JWT_SECRET");
        Jwt jwt = new JwtHS256(jwtIssuer, jwtSecret);

        String contentType = dotenv.get("CONTENT_TYPE");
        String corsOrigins = dotenv.get("CORS_ORIGINS");
        String corsMethods = dotenv.get("CORS_METHODS");
        String corsHeaders = dotenv.get("CORS_HEADERS");
        App app = new App(
                Type.MEMORY,
                db,
                jwt,
                new StandardOutputLogger(),
                contentType != null ? contentType : "application/json",
                corsOrigins != null ? corsOrigins : "*",
                corsMethods != null ? corsMethods : "POST, GET, PUT, DELETE, OPTIONS",
                corsHeaders != null ? corsHeaders : ""
        );

        String strPort = dotenv.get("PORT");
        int port = Integer.valueOf(strPort != null ? strPort : "8080");
        app.build(port).start();
    }

}
