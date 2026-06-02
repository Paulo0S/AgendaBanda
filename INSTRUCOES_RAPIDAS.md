# Instruções rápidas

1. Instale o SQL Server Express e o SQL Server Management Studio.
2. Execute os scripts na ordem:
   - `database/00_create_login.sql`
   - `database/01_create_schema.sql`
   - `database/02_insert_data.sql`
3. Rode o backend com `mvn spring-boot:run` dentro de `backend`.
4. Rode o frontend com `npm install` e `npm run dev` dentro de `web`.
5. Acesse `http://localhost:5173`.

Se o SQL Server não estiver em `localhost:1433`, crie um `.env` em `backend` seguindo `backend/.env.example`.

Logins:

- `admin / admin`
- `usuario / usuario`
- `teste / teste`
