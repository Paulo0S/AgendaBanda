USE master;
GO

IF DB_ID('AgendaBanda') IS NULL
BEGIN
    CREATE DATABASE AgendaBanda;
END
GO

IF EXISTS (SELECT 1 FROM sys.sql_logins WHERE name = 'agenda_user')
BEGIN
    ALTER LOGIN agenda_user
        WITH PASSWORD = 'agenda123',
        CHECK_POLICY = OFF,
        CHECK_EXPIRATION = OFF;
END
ELSE
BEGIN
    CREATE LOGIN agenda_user
        WITH PASSWORD = 'agenda123',
        CHECK_POLICY = OFF,
        CHECK_EXPIRATION = OFF;
END
GO

ALTER LOGIN agenda_user WITH DEFAULT_DATABASE = AgendaBanda;
GO

USE AgendaBanda;
GO

IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = 'agenda_user')
BEGIN
    CREATE USER agenda_user FOR LOGIN agenda_user;
END
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.database_role_members drm
    JOIN sys.database_principals role_principal
        ON drm.role_principal_id = role_principal.principal_id
    JOIN sys.database_principals member_principal
        ON drm.member_principal_id = member_principal.principal_id
    WHERE role_principal.name = 'db_owner'
      AND member_principal.name = 'agenda_user'
)
BEGIN
    ALTER ROLE db_owner ADD MEMBER agenda_user;
END
GO
