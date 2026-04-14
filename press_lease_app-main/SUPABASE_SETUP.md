# Supabase Setup

このアプリは、画面はそのままで DB 接続先だけを Supabase PostgreSQL に切り替えられます。

## 1. Supabase で確認する値

Render のような通常のサーバー環境では、Supabase ダッシュボードの `Connect` 画面に出る pooler 接続情報を使うのが安全です。

- `DB_URL`
  例: `jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres?sslmode=require`
- `DB_USERNAME`
  例: `postgres.<project-ref>`
- `DB_PASSWORD`
  Supabase の Database Password

`db.<project-ref>.supabase.co:5432` の direct connection は、環境によっては接続できません。Render では pooler を優先してください。

## 2. ローカル実行時の設定例

```powershell
$env:SPRING_PROFILES_ACTIVE="supabase"
$env:APP_DATABASE_PLATFORM="supabase"
$env:DB_URL="jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres?sslmode=require"
$env:DB_USERNAME="postgres.<project-ref>"
$env:DB_PASSWORD="<your-database-password>"
```

## 3. 起動

```powershell
./mvnw spring-boot:run
```

JAR で起動する場合:

```powershell
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## 4. 補足

- HTML / Thymeleaf 側の画面構成は変わりません。
- `APP_DATABASE_PLATFORM=supabase` を設定すると PostgreSQL 用の初期化 SQL を使います。
- このアプリは `DB_URL` からドライバを自動判定できるようにしてあるため、`SPRING_PROFILES_ACTIVE=supabase` を入れ忘れても PostgreSQL 接続で起動できます。
