# Belajar JDBC dengan PostgreSQL

Pembahasan materi tentang JDBC:

1. What is JDBC / Java Database Connectivity
2. Setup Project JDBC with Maven Archetype - quick starter archetype
3. Opening / Closing Connection
4. Database Versioning with flyway database migration
5. Select statement with class Statement
6. Select statement with class PreparedStatement
7. SQL Injection handler
8. Insert, Update, and Delete using class PreparedStatement
9. Pagination using class PreparedStatement
10. Array as Parameter using class PreparedStatement 
11. Transactional using JDBC
12. Batch Processing

## Start migrate

```bash
mvn clean \
-Dflyway.user=bootcamp \
-Dflyway.password=admin \
-Dflyway.schemas=public,perpustakaan,bank,hr \
-Dflyway.url=jdbc:postgresql://localhost:5432/bootcamp \
flyway:migrate
```
