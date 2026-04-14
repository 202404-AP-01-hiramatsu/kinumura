# Supabase Setup

このアプリは静的 HTML から Supabase API を直接呼ぶ構成ではなく、Spring Boot から PostgreSQL に直接接続する構成です。
そのため、Supabase を使う場合も `Project URL` と `anon key` をブラウザへ埋め込む必要はありません。

## 1. Supabase 側の準備

1. Supabase でプロジェクトを作成します。
2. `Region` は `Northeast Asia (Tokyo)` を選びます。
3. `Database Password` は必ず控えます。
4. SQL Editor で [supabase-schema.sql](C:/academia/src/202512/kinumura/press_lease_app-main/supabase-schema.sql:1) の内容を実行します。

## 2. Render に設定する環境変数

Supabase ダッシュボードの `Connect` 画面にある pooler 接続情報を使ってください。

- `APP_DATABASE_PLATFORM=supabase`
- `DB_URL=jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres?sslmode=require`
- `DB_USERNAME=postgres.<project-ref>`
- `DB_PASSWORD=<SupabaseのDatabase Password>`

補足:

- `db.<project-ref>.supabase.co:5432` の direct connection は、Render では接続できないことがあります。
- このアプリは `DB_URL` から PostgreSQL ドライバを自動判定できるため、`SPRING_PROFILES_ACTIVE=supabase` は必須ではありません。

## 3. 既存アプリとの対応

- `/master`
  `master_setting` テーブルを使います。
- `/todos`
  `todo` テーブルを使います。
- 伝票系画面
  `slip` `slip_detail` `slip_media` を使います。

## 4. ToDo サンプル相当の動作

静的 HTML + Supabase SDK の代わりに、このアプリではサーバー API を使います。

- `GET /todos`
  ToDo 一覧取得
- `POST /todos`
  ToDo 追加

追加例:

```json
{
  "task": "新しいタスク"
}
```

または

```json
{
  "title": "新しいタスク"
}
```
