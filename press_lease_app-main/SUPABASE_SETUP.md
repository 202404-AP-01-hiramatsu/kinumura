# Supabase Setup

このアプリは画面構成を変えずに、サーバー側のDB接続先だけを Supabase PostgreSQL に切り替えられます。

## 1. Supabase で確認する値

必要なのは `Project URL` や `publishable key` ではなく、Database 接続情報です。

- `DB_URL`
  例: `jdbc:postgresql://db.<project-ref>.supabase.co:5432/postgres?sslmode=require`
- `DB_USERNAME`
  例: `postgres`
- `DB_PASSWORD`
  Supabase の Database Password

## 2. 起動時に設定する環境変数

PowerShell 例:

```powershell
$env:SPRING_PROFILES_ACTIVE="supabase"
$env:APP_DATABASE_PLATFORM="supabase"
$env:DB_URL="jdbc:postgresql://db.<project-ref>.supabase.co:5432/postgres?sslmode=require"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="<your-database-password>"
```

## 3. 起動コマンド

```powershell
./mvnw spring-boot:run
```

またはビルド済み JAR の場合:

```powershell
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## 4. 補足

- 既存画面の HTML/Thymeleaf は変更不要です。
- 起動時に PostgreSQL 用の初期化 SQL が実行され、必要テーブルを作成します。
- 既存の MySQL 起動もそのまま残してあります。
