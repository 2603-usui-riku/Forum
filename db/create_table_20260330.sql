-- 1. ジャンルを分ける「板」
CREATE TABLE boards (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    slug VARCHAR(20) UNIQUE NOT NULL,
    description VARCHAR(255)
);

-- 2. 話題をまとめる「スレッド」
CREATE TABLE threads (
    id SERIAL PRIMARY KEY,
    board_id INTEGER NOT NULL REFERENCES boards(id) ON DELETE CASCADE,
    title VARCHAR(100) NOT NULL,
    post_count INTEGER DEFAULT 0,
    last_post_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. 実際の書き込み「ポスト」
CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    thread_id INTEGER NOT NULL REFERENCES threads(id) ON DELETE CASCADE,
    post_num INTEGER NOT NULL,
    name VARCHAR(50) DEFAULT '名無しさん',
    content TEXT NOT NULL,
    del_password VARCHAR(255),
    ip_address VARCHAR(45),
    is_edited BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);