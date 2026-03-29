# FinFun：方方面面記帳，讓理財變有趣

    USE finance_app;
    
    -- 使用者表
    CREATE TABLE users (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        name                             VARCHAR(100)   NOT NULL,                        -- 使用者名稱
        email                            VARCHAR(100)   NOT NULL UNIQUE,                 -- 使用者電子郵件
        password                         VARCHAR(255)   NOT NULL,                        -- 使用者密碼
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 角色表
    CREATE TABLE roles (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)    NOT NULL UNIQUE,                 -- ADMIN, STAFF, VIP, USER
        name                             VARCHAR(50)    NOT NULL,                        -- 管理者、員工、高級使用者、使用者
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 使用者與角色關聯表
    CREATE TABLE user_has_roles (
        user_id                          BIGINT         NOT NULL,
        role_id                          BIGINT         NOT NULL,
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)
        PRIMARY KEY (user_id, role_id),
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
        FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
    );

    -- 檔案類型表
    CREATE TABLE file_types (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)    NOT NULL UNIQUE,                 -- 代號 (JPG、PNG、PDF)
        name                             VARCHAR(50)    NOT NULL,                        -- 名稱 (JPEG image、PNG image、PDF file)
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 檔案表
    CREATE TABLE files (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        user_id                          BIGINT         NULL,                            -- 誰上傳的檔案
        entity_type                      VARCHAR(50)    NOT NULL,                        -- 使用在的表 users、ledgers、accounts、category_groups、categories、merchants
        entity_id                        BIGINT         NOT NULL,                        -- 對應表的id
        file_role                        VARCHAR(50),                                    -- 檔案的角色 avatar、logo、cover、receipt
        file_url                         VARCHAR(255)   NOT NULL, 
        file_type_id                     BIGINT         NOT NULL,                        -- 檔案類型 JPG、PNG、GIF、TXT 
        file_size                        BIGINT,                                         -- 檔案大小 
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)
        INDEX idx_files_entity (entity_type, entity_id),
        INDEX idx_files_user (user_id),
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
        FOREIGN KEY (file_type_id) REFERENCES file_types(id)
    );

    -- 國別表
    CREATE TABLE countries (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        iso2                             CHAR(2)        NOT NULL UNIQUE,                 -- ISO 3166-1國別碼 (兩碼)
        iso3                             CHAR(3)        NULL,                            -- ISO 3166-1國別碼 (三碼)
        iso_numeric                      CHAR(3)        NULL UNIQUE,                     -- ISO 3166-1國別碼 (數字)
        phone_code                       VARCHAR(10)    NULL,                            -- 國際電話區碼
    
        name                             VARCHAR(50)    NOT NULL,                        -- 國家名稱 (英文)
        native_name                      VARCHAR(100)   NOT NULL,                        -- 國家名稱 (本地文字)
        image_url                        VARCHAR(255)   NULL,                            -- 國旗

        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 時區表
    CREATE TABLE timezones (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)   NOT NULL,                        -- 時區代碼 (UTC、EST、CST)
        iana_name                        VARCHAR(50)   NOT NULL UNIQUE,                 -- IANA 時區名稱 (Etc/UTC、America/New_York、Asia/Taipei)
        name                             VARCHAR(100),                                  -- 名稱 (協調世界時間、美國東部時間、中原標準時間)
        utc_offset                       TIME          NOT NULL,                        -- 偏移 (+00:00:00、-05:00:00、08:00:00)，為時區參考值，實際為 IANA 計算
        
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 國別時區關聯表
    CREATE TABLE country_timezones (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        country_id                       BIGINT        NOT NULL,
        timezone_id                      BIGINT        NOT NULL,

        is_default                       BOOLEAN       DEFAULT TRUE,                    -- 是否為預設時區

        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)

        UNIQUE (country_id, timezone_id),

        FOREIGN KEY (country_id) REFERENCES countries(id),
        FOREIGN KEY (timezone_id) REFERENCES timezones(id)
    );

    -- 貨幣表
    CREATE TABLE currencies (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        code                             CHAR(3)        NOT NULL UNIQUE,                 -- 貨幣碼 (TWD、USD、JPY...)
        name                             VARCHAR(50)    NOT NULL,                        -- 貨幣名稱 (新台幣、美元、日幣...)
        symbol                           VARCHAR(10)    NULL,                            -- 貨幣符號 (NT$、$、¥、€、£、₩、₽...)
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 國別貨幣關聯表
    CREATE TABLE currency_countries (
        currency_id                      BIGINT         NOT NULL,
        country_id                       BIGINT         NOT NULL,

        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)

        PRIMARY KEY (currency_id, country_id),
        FOREIGN KEY (currency_id) REFERENCES currencies(id) ON DELETE CASCADE ON UPDATE CASCADE,
        FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE CASCADE ON UPDATE CASCADE
    );
    
    -- 匯率表
    CREATE TABLE exchange_rates (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,

        base_currency_id                 BIGINT         NOT NULL,                        -- 基準幣別 (USD)
        quote_currency_id                BIGINT         NOT NULL,                        -- 報價幣別 (TWD)

        rate                             DECIMAL(18,8)  NOT NULL,                        -- 匯率 (例 1 USD = 30 TWD)
        rate_date                        DATE           NOT NULL,                        -- 匯率日期

        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)

        UNIQUE (base_currency_id, quote_currency_id, rate_date),
        INDEX idx_exchange_rate_lookup (base_currency_id, quote_currency_id),
        CHECK (base_currency_id <> quote_currency_id),

        FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
        FOREIGN KEY (quote_currency_id) REFERENCES currencies(id)
    );

    -- 投資市場類型表
    CREATE TABLE market_types (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)    NOT NULL UNIQUE,                 -- 代號 (GEOGRAPHIC、ECONOMIC、FINANCIAL、POLITICS)
        name                             VARCHAR(50)    NOT NULL,                        -- 名稱 (地理、經濟、金融、政治)
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 投資市場表
    CREATE TABLE markets (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)    NOT NULL UNIQUE,                 -- 市場代號 (TW、US、JP、CN、EU、EM、EMERGING、DEVELOPED、ASIA_TIGERS)
        name                             VARCHAR(50)    NOT NULL,                        -- 市場名稱 (台灣、美國、日本、中國、歐洲、新興市場、開發中國家、亞洲四小龍)
        market_type_id                   BIGINT         NOT NULL,                        -- 市場種類
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)
        UNIQUE(name),
        FOREIGN KEY (market_type_id) REFERENCES market_types(id)
    );

    -- 市場國別關聯表
    CREATE TABLE market_countries (
        market_id                        BIGINT         NOT NULL,
        country_id                       BIGINT         NOT NULL,
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)
        PRIMARY KEY (market_id, country_id),
        FOREIGN KEY (market_id) REFERENCES markets(id) ON DELETE CASCADE,
        FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE CASCADE
    );

    -- 金融機構角色表 (以業務區分)
    CREATE TABLE financial_institution_roles (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)    NOT NULL UNIQUE,                 -- 代號
        name                             VARCHAR(50)    NOT NULL,                        -- 名稱
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 金融機構集團表
    CREATE TABLE financial_institution_groups (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        country_id                       BIGINT         NOT NULL,                        -- 金融機構集團總部國
        name                             VARCHAR(100)   NOT NULL,                        -- 金融機構集團名稱 (例如：國泰金控、富邦金控)
        image_url                        VARCHAR(255)   NULL,                            -- 金融機構集團logo
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)
        UNIQUE(name, country_id),
        FOREIGN KEY (country_id) REFERENCES countries(id)
    );

    -- 金融機構表
    CREATE TABLE financial_institutions (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        country_id                       BIGINT         NOT NULL,                        -- 金融機構總部國
        financial_institution_group_id   BIGINT         NULL,                            -- 金融機構集團
        name                             VARCHAR(100)   NOT NULL,                        -- 金融機構名稱 (例如：國泰世華銀行、台本富邦銀行、國泰人壽、IB、Binance)
        image_url                        VARCHAR(255)   NULL,                            -- 金融機構logo
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)
        UNIQUE(name, country_id),
        FOREIGN KEY (country_id) REFERENCES countries(id),
        FOREIGN KEY (financial_institution_group_id) REFERENCES financial_institution_groups(id)
    );

    -- 金融機構國家與角色關聯表(金融機構在某國家的業務角色)
    CREATE TABLE financial_institution_country_roles (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        country_id                       BIGINT         NOT NULL,
        financial_institution_id         BIGINT         NOT NULL,
        financial_institution_role_id    BIGINT         NOT NULL,

        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)

        UNIQUE(country_id, financial_institution_id, financial_institution_role_id),

        INDEX idx_fi (financial_institution_id),
        INDEX idx_country (country_id),
        INDEX idx_role (financial_institution_role_id),
        
        FOREIGN KEY (country_id) REFERENCES countries(id),
        FOREIGN KEY (financial_institution_id) REFERENCES financial_institutions(id),
        FOREIGN KEY (financial_institution_role_id) REFERENCES financial_institution_roles(id)
    );

    -- 頻率類型表
    CREATE TABLE frequency_types (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)    NOT NULL UNIQUE,                 -- repayment、payment、dividend、payout
        name                             VARCHAR(50)    NOT NULL,                        -- 還款、配息
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 頻率表(付款、收款、配息、不配息)
    CREATE TABLE frequencies (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        frequency_type_id                BIGINT         NOT NULL,
        code                             VARCHAR(30)    NOT NULL,                        -- year、month、week、day、monthly、quarterly、semi_annual、annual、none
        name                             VARCHAR(50)    NOT NULL,                        -- 年繳、月繳、週繳、日繳、月配、季配、半年配、年配、不配息
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)
        UNIQUE(code, frequency_type_id),
        FOREIGN KEY (frequency_type_id) REFERENCES frequency_types(id)
    );

    -- 單位類型表
    CREATE TABLE unit_types (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)    NOT NULL UNIQUE,                 -- inventory、financial、commodity
        name                             VARCHAR(50)    NOT NULL,                        -- 存貨、金融商品、商品期貨
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 單位表
    CREATE TABLE units (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)    NOT NULL UNIQUE,                 -- 單位代碼 (例如 pkg、bottle、piece、contract、share、oz、barrel)
        name                             VARCHAR(50)    NOT NULL,                        -- 名稱 (例如 包、瓶、個、合約、股、盎司、桶)
        unit_type_id                     BIGINT         NOT NULL,
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)
        FOREIGN KEY (unit_type_id) REFERENCES unit_types(id)
    );

    -- 帳本種類表
    CREATE TABLE ledger_types (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)    NOT NULL UNIQUE,                 -- cashflow、investment、debt、receivable、fixed_asset、inventory
        name                             VARCHAR(50)    NOT NULL,                        -- 收支帳、投資帳、負債帳、應收帳、固定資產帳、存貨帳
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL                             -- 刪除時間 (由後端寫入)
    );


    -- 帳本表
    CREATE TABLE ledgers (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        user_id                          BIGINT         NOT NULL,
        ledger_type_id                   BIGINT         NOT NULL,
        
        name                             VARCHAR(100)   NOT NULL,                        -- 帳本名稱
        note                             VARCHAR(255),                                   -- 備註
        
        created_date                     DATETIME       NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                            -- 刪除時間 (由後端寫入)

        UNIQUE(user_id, name),
        
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
        FOREIGN KEY (ledger_type_id) REFERENCES ledger_types(id)
    );

    -- 帳本成員角色表
    CREATE TABLE ledger_member_roles (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)    NOT NULL UNIQUE,                -- OWNER、ADMIN、EDITOR、VIEWER
        name                             VARCHAR(50)    NOT NULL,                       -- 擁有者、管理員、可編輯、只能看、
        note                             VARCHAR(255),
        created_date                     DATETIME       NOT NULL,                       -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                       -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL                            -- 刪除時間 (由後端寫入)
    );

    -- 帳本成員表 (可設定多人共同維護一個帳戶)
    CREATE TABLE ledger_members (
        id                               BIGINT         AUTO_INCREMENT PRIMARY KEY,
        user_id                          BIGINT         NOT NULL,
        ledger_id                        BIGINT         NOT NULL,
        role_id                          BIGINT         NOT NULL,                       -- 權限設定
        joined_date                      DATETIME       NOT NULL,                       -- 加入時間
        created_date                     DATETIME       NOT NULL,                       -- 建立時間 (由後端寫入)
        updated_date                     DATETIME       NOT NULL,                       -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME       NULL,                           -- 刪除時間 (由後端寫入)
        UNIQUE KEY uk_ledger_user (ledger_id, user_id),
        FOREIGN KEY (ledger_id) REFERENCES ledgers(id) ON DELETE CASCADE,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
        FOREIGN KEY (role_id) REFERENCES ledger_member_roles(id)
    );

    -- 帳戶種類表
    CREATE TABLE account_types (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        ledger_type_id                   BIGINT        NOT NULL,
        
        code                             VARCHAR(30)   NOT NULL,
        name                             VARCHAR(50)   NOT NULL,                        -- 帳戶性質（如：
                                                                                        -- 收支帳下的現金 cash、銀行 bank、信用卡 credit_card ，
                                                                                        -- 投資帳下的股票 stock、基金 fund、債券 bond、外幣 forex、虛擬貨幣 crypto、
                                                                                        --          貴金屬 gold/silver、定存 fixed_deposits、保險 insurance、
                                                                                        --          期貨 futures、選擇權 option，
                                                                                        -- 負債帳下的房貸 mortgage、信貸 personal_loan、車貸 auto_loan，
                                                                                        -- 應收帳下的借出款 lent，
                                                                                        -- 固定資產帳下的房貸產 property、汽車 automobile、機車 motorcycle、家電、3C
                                                                                        -- 存貨帳下的生活用品或是備品
        direction                        TINYINT       NOT NULL,                        -- 正向 / 負向
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)

        FOREIGN KEY (ledger_type_id) REFERENCES ledger_types(id),
        UNIQUE (ledger_type_id, code)
    );

    -- 帳戶表
    CREATE TABLE accounts (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        ledger_id                        BIGINT        NOT NULL,
        account_type_id                  BIGINT        NOT NULL,
        currency_id                      BIGINT        NOT NULL,

        name                             VARCHAR(100)  NOT NULL,
        initial_balance                  DECIMAL(18,8) NOT NULL DEFAULT 0,              -- 初始餘額
        note                             VARCHAR(255),                                  -- 備註
        
        created_date                     DATETIME      NOT NULL,		                -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)

        UNIQUE(ledger_id, name),
        INDEX(ledger_id),
        INDEX(account_type_id),
        INDEX(currency_id),
        FOREIGN KEY (ledger_id) REFERENCES ledgers(id) ON DELETE CASCADE,
        FOREIGN KEY (account_type_id) REFERENCES account_types(id),
        FOREIGN KEY (currency_id) REFERENCES currencies(id)
    );

    -- 帳戶表子表 (收支帳 → 帳戶)
    CREATE TABLE bank_accounts (
        account_id                       BIGINT        PRIMARY KEY,
        financial_institution_id         BIGINT        NULL,
        interest_rate                    DECIMAL(18,8) NOT NULL,                        -- 利率 %
        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (financial_institution_id) REFERENCES financial_institutions(id)
    );

    -- 帳戶表子表 (收支帳 → 信用卡)
    CREATE TABLE credit_card_accounts (
        account_id                       BIGINT        PRIMARY KEY,
        financial_institution_id         BIGINT        NULL,
        
        cycle_day                        TINYINT       NOT NULL,		                -- 結帳日 (1~31)
        due_day                          TINYINT       NOT NULL,                        -- 繳款日 (1~31)
        credit_limit                     DECIMAL(18,8) NULL,                            -- 信用額度
        annual_fee                       DECIMAL(18,8) NULL,                            -- 年費
        
        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (financial_institution_id) REFERENCES financial_institutions(id)
    );
    
    -- 帳戶表子表 (投資帳 → 股票、ETF、基金、債券、外幣、虛擬貨幣、貴金屬、期貨 futures、選擇權 option，）
    CREATE TABLE investment_accounts (
        account_id                       BIGINT        PRIMARY KEY,
        financial_institution_id         BIGINT        NULL,
        market_id                        BIGINT        NULL,
        risk_level                       TINYINT,                                       -- 風險等級（1~5，可選）
        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (financial_institution_id) REFERENCES financial_institutions(id),
        FOREIGN KEY (market_id) REFERENCES markets(id)
    );

    -- 帳戶表子表 (投資帳 → 定存、保險）
    CREATE TABLE term_investment_accounts (
        account_id                       BIGINT        PRIMARY KEY,
        financial_institution_id         BIGINT        NULL,
        interest_type                    VARCHAR(20)   NOT NULL,                        -- 固定 fixed / 流動 floating
        interest_rate                    DECIMAL(18,8) NOT NULL,                        -- 利率 %
        start_date                       DATE          NOT NULL,                        -- 開始日
        end_date                         DATE          NOT NULL,                        -- 到期日
        early_termination_allowed        BOOLEAN,                                       -- 是否可提前贖回
        payout_frequency_id              BIGINT        NULL,                            -- 配息類型
        penalty_rate                     DECIMAL(18,8) NULL,                            -- 違約利率
        status                           VARCHAR(20)   NOT NULL,                        -- 狀態
        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (financial_institution_id) REFERENCES financial_institutions(id),
        FOREIGN KEY (payout_frequency_id) REFERENCES frequencies(id)
    );

    -- 帳戶表子表 (負債帳 → 房貸、車貸、信貸）
    CREATE TABLE debt_accounts (
        account_id                       BIGINT        PRIMARY KEY,
        financial_institution_id         BIGINT        NULL,
        repayment_frequency_id           BIGINT        NOT NULL,
        
        interest_rate                    DECIMAL(18,8) NOT NULL,                        -- 利率 %

        cycle_day                        TINYINT       NOT NULL,                        -- 結帳日 (1~31)
        due_day                          TINYINT       NOT NULL,                        -- 繳款日 (1~31)
        
        start_date                       DATE          NOT NULL,                        -- 開始日
        end_date                         DATE          NOT NULL,                        -- 到期日
        
        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (financial_institution_id) REFERENCES financial_institutions(id),
        FOREIGN KEY (repayment_frequency_id) REFERENCES frequencies(id)
    );

    -- 負債合約表
    CREATE TABLE debt_contracts (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        account_id                       BIGINT        NOT NULL,

        total_amount                     DECIMAL(18,8) NOT NULL,                        -- 總金額
        total_terms                      INT           NOT NULL,                        -- 總期數

        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)

        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
    );

    -- 債務人表
    CREATE TABLE debtors (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        name                             VARCHAR(100)  NOT NULL,
        phone                            VARCHAR(50),
        email                            VARCHAR(100),
        address                          VARCHAR(255),
        notes                            VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 帳戶表子表 (應收帳款)
    CREATE TABLE receivable_accounts (
        account_id                       BIGINT        PRIMARY KEY,
        debtor_id                        BIGINT        NOT NULL,
        repayment_frequency_id           BIGINT        NOT NULL,
        
        interest_rate                    DECIMAL(18,8) NOT NULL,                        -- 利率 %
        cycle_day                        TINYINT       NOT NULL,                        -- 結帳日 (1~31)
        due_day                          TINYINT       NOT NULL,                        -- 繳款日 (1~31)
        
        start_date                       DATE          NOT NULL,                        -- 開始日
        end_date                         DATE          NOT NULL,                        -- 到期日
        
        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (debtor_id) REFERENCES debtors(id),
        FOREIGN KEY (repayment_frequency_id) REFERENCES frequencies(id)
    );

    -- 應收帳款合約表
    CREATE TABLE receivable_contracts (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        account_id                       BIGINT        NOT NULL,
        debtor_id                        BIGINT        NOT NULL,
        
        total_amount                     DECIMAL(18,8) NOT NULL,                        -- 總金額
        interest_rate                    DECIMAL(18,8) NOT NULL,                        -- 利率 %

        start_date                       DATE          NOT NULL,                        -- 開始日
        end_date                         DATE          NOT NULL,                        -- 到期日

        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)

        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (debtor_id) REFERENCES debtors(id)
    );

    -- 固定資產分類表
    CREATE TABLE asset_categories (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)   NOT NULL UNIQUE,                 -- 房屋 house、車輛 car、家電 appliance、3C electronics
        name                             VARCHAR(50)   NOT NULL,                        -- 名稱
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 折舊方法表
    CREATE TABLE depreciation_methods (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)   NOT NULL UNIQUE,                 -- straight_line、declining_balance、double_declining、sum_of_years
        name                             VARCHAR(50)   NOT NULL,                        -- 直線法、餘額遞減法/定率法、雙倍餘額遞減法、年數總和法
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 帳戶表子表 (固定資產帳 → 房屋、車輛、家電、3C)
    CREATE TABLE fixed_asset_accounts (
        account_id                       BIGINT        PRIMARY KEY,
        asset_category_id                BIGINT        NULL,

        asset_tag                        VARCHAR(50)   UNIQUE,                          -- 識別碼
        purchase_date                    DATE          NOT NULL,                        -- 購買日
        purchase_price                   DECIMAL(18,8) NOT NULL,                        -- 購買價格
        useful_life_years                INT,                                           -- 使用年限
        depreciation_method_id           BIGINT        NULL,
        salvage_value                    DECIMAL(18,8) NULL,                            -- 殘值
        location                         VARCHAR(100),                                  -- 位置
        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (asset_category_id) REFERENCES asset_categories(id),
        FOREIGN KEY (depreciation_method_id) REFERENCES depreciation_methods(id)
    );

    -- 存貨分類表
    CREATE TABLE inventory_categories (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)   NOT NULL UNIQUE,                 -- 食品 food、飲品 beverage、消耗品 consumable、備件 spare_part、辦公用品office_supplies
        name                             VARCHAR(50)   NOT NULL,                        -- 名稱
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 存貨成本方法表
    CREATE TABLE cost_methods (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)   NOT NULL UNIQUE,                 -- 先進先出法 fifo、後進先出法 lifo、加權平均法 average
        name                             VARCHAR(50)   NOT NULL,                        -- 名稱
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 帳戶表子表 (存貨帳 → 生活用品 / 備品)
    CREATE TABLE inventory_accounts (
        account_id                       BIGINT        PRIMARY KEY,
        inventory_category_id            BIGINT        NULL,
        unit_id                          BIGINT        NOT NULL,                        -- 單位
        quantity                         DECIMAL(18,8) NOT NULL DEFAULT 0,              -- 目前數量
        warning_level                    DECIMAL(18,8) NULL,                            -- 低於提醒
        cost_method_id                   BIGINT        NULL,
        expiry_date                      DATE,                                          -- 有效期限
        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (inventory_category_id) REFERENCES inventory_categories(id),
        FOREIGN KEY (cost_method_id) REFERENCES cost_methods(id),
        FOREIGN KEY (unit_id) REFERENCES units(id)
    );

    -- 投資產品種類表
    CREATE TABLE investment_product_types (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)   NOT NULL UNIQUE,                 -- stock、etf、fund、bond、crypto、forex、gold、futures、option
        name                             VARCHAR(50)   NOT NULL,                        -- 股票、ETF、基金、債券、虛擬貨幣、外匯、黃金、期貨、選擇權
        is_derivative                    BOOLEAN       DEFAULT FALSE,                   -- 是否為衍生性商品
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 交易所表
    CREATE TABLE exchanges (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)   NOT NULL UNIQUE,                 -- NASDAQ、NYSE、TSE、BINANCE
        name                             VARCHAR(50)   NOT NULL,                        -- 美國納斯達克交易所、紐約證券交易所
        country_id                       BIGINT        NULL,                            -- 所屬國家
        timezone_id                      BIGINT        NULL,                            -- 時區
        image_url                        VARCHAR(255)  NULL,
        note                             VARCHAR(255),
        
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)
        
        FOREIGN KEY (country_id) REFERENCES countries(id),
        FOREIGN KEY (timezone_id) REFERENCES timezones(id)
    );

    -- 投資產品表
    CREATE TABLE investment_products (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        product_type_id                  BIGINT        NOT NULL,
        
        market_id                        BIGINT        NULL,                            -- 所屬市場 (美股、台股、幣圈)
        currency_id                      BIGINT        NOT NULL,                        -- 報價幣別 (USD、TWD)
        exchange_id                      BIGINT        NULL,                            -- 交易所

        code                             VARCHAR(50)   NOT NULL,                        -- 商品代碼 (AAPL、TSLA、BTC、0050)
        name                             VARCHAR(100)  NOT NULL,                        -- 商品名稱 (Apple Inc.)
        isin                             VARCHAR(20),                                   -- 國際證券識別碼 ISIN（可為 NULL）
        is_active                        BOOLEAN       DEFAULT TRUE,                    -- 是否仍可交易

        unit_id                          BIGINT        NULL,                            -- 商品單位 (口、股、張)
        image_url                        VARCHAR(255)  NULL,
        note                             VARCHAR(255),

        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)

        UNIQUE(code, exchange_id, market_id),
        FOREIGN KEY (product_type_id) REFERENCES investment_product_types(id),
        FOREIGN KEY (market_id) REFERENCES markets(id),
        FOREIGN KEY (currency_id) REFERENCES currencies(id),
        FOREIGN KEY (exchange_id) REFERENCES exchanges(id),
        FOREIGN KEY (unit_id) REFERENCES units(id)
    );

    -- 基金類型表
    CREATE TABLE fund_types (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)   NOT NULL UNIQUE,                 -- equity、bond、balanced、money_market、index、etc
        name                             VARCHAR(50)   NOT NULL,                        -- 股票型、債券型、平衡型、貨幣市場型、指數型、其他
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 投資產品表子表 (基金)
    CREATE TABLE fund_products (
        investment_product_id            BIGINT        PRIMARY KEY,
        
        financial_institution_id         BIGINT        NULL,                            -- 發行金融機構
        expense_ratio                    DECIMAL(18,8) NOT NULL,                        -- 總費用率
        dividend_frequency_id            BIGINT        NULL,                            -- 配息頻率
        fund_type_id                     BIGINT        NULL,                            -- 基金類型
        
        FOREIGN KEY (investment_product_id) REFERENCES investment_products(id) ON DELETE CASCADE,
        FOREIGN KEY (financial_institution_id) REFERENCES financial_institutions(id),
        FOREIGN KEY (dividend_frequency_id) REFERENCES frequencies(id),
        FOREIGN KEY (fund_type_id) REFERENCES fund_types(id)
    );

    -- 債券發行單位表
    CREATE TABLE bond_issuers (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(50)   NOT NULL UNIQUE,                 -- 代碼
        name                             VARCHAR(100)  NOT NULL,                        -- 發行機構名稱
        country_id                       BIGINT        NOT NULL,                        -- 所屬國家
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)
        FOREIGN KEY (country_id) REFERENCES countries(id)
    );

    -- 債券種類表
    CREATE TABLE bond_types (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)   NOT NULL UNIQUE,                 -- government、corporate、municipal
        name                             VARCHAR(50)   NOT NULL,                        -- 政府債、公司債、市政債
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );
    
    -- 投資產品表子表 (債券)
    CREATE TABLE bond_products (
        investment_product_id            BIGINT        PRIMARY KEY,

        bond_issuer_id                   BIGINT        NULL,                            -- 發行機構
        coupon_rate                      DECIMAL(18,8) NOT NULL,                        -- 票面利率
        coupon_frequency_id              BIGINT        NULL,                            -- 票面配息頻率
        maturity_date                    DATE          NOT NULL,                        -- 到期日
        face_value                       DECIMAL(18,8) NOT NULL,                        -- 面額
        bond_type_id                     BIGINT        NULL,                            -- 債券種類

        FOREIGN KEY (investment_product_id) REFERENCES investment_products(id) ON DELETE CASCADE,
        FOREIGN KEY (bond_issuer_id) REFERENCES bond_issuers(id),
        FOREIGN KEY (coupon_frequency_id) REFERENCES frequencies(id),
        FOREIGN KEY (bond_type_id) REFERENCES bond_types(id)
    );

    -- 投資產品表子表 (期貨)
    CREATE TABLE derivative_products (
        investment_product_id            BIGINT        PRIMARY KEY,
        contract_size                    DECIMAL(18,8) NOT NULL,                        -- 每張合約的標的數量
        expiration_date                  DATE          NOT NULL,                        -- 到期日
        FOREIGN KEY (investment_product_id) REFERENCES investment_products(id) ON DELETE CASCADE
    );

    -- 選擇權類型表
    CREATE TABLE option_types (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)   NOT NULL UNIQUE,                 -- CALL、PUT
        name                             VARCHAR(50)   NOT NULL,                        -- 買權 Call Option、賣權 Put Option
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 投資產品表子表 (選擇權)
    CREATE TABLE option_products (
        investment_product_id            BIGINT        PRIMARY KEY,
        strike_price                     DECIMAL(18,8) NOT NULL,                        -- 履約價
        option_type_id                   BIGINT        NOT NULL,
        FOREIGN KEY (investment_product_id) REFERENCES derivative_products(investment_product_id),
        FOREIGN KEY (option_type_id) REFERENCES option_types(id)
    );

    -- 投資產品歷史價格表
    CREATE TABLE investment_price_history (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,

        investment_product_id            BIGINT        NOT NULL,

        price                            DECIMAL(18,8) NOT NULL,
        price_date                       DATE          NOT NULL,

        created_date                     DATETIME      NOT NULL,		                 -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                 -- 更新時間 (由後端寫入)

        INDEX(investment_product_id, price_date),
        INDEX(price_date),

        UNIQUE (investment_product_id, price_date),
        FOREIGN KEY (investment_product_id) REFERENCES investment_products(id)
    );

    -- 分類類型表
    CREATE TABLE category_types (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(30)   NOT NULL UNIQUE,                 -- income、expense、transfer
        name                             VARCHAR(50)   NOT NULL,                        -- 收入、支出、轉帳
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );
    
    -- 大分類表
    CREATE TABLE category_groups (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        ledger_id                        BIGINT        NOT NULL,
        name                             VARCHAR(50)   NOT NULL,                        -- 大分類名稱
        category_type_id                 BIGINT        NOT NULL,                        -- 分類類型
        
        created_date                     DATETIME      NOT NULL,		                -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)

        UNIQUE(ledger_id, name),
        
        FOREIGN KEY (ledger_id) REFERENCES ledgers(id) ON DELETE CASCADE,
        FOREIGN KEY (category_type_id) REFERENCES category_types(id)
    );

    -- 小分類表
    CREATE TABLE categories (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        ledger_id                        BIGINT        NOT NULL,
        category_group_id                BIGINT        NOT NULL,		                -- (對應大分類)
        name                             VARCHAR(50)   NOT NULL,                        -- 小分類名稱
        created_date                     DATETIME      NOT NULL,		                -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)
        UNIQUE(ledger_id, category_group_id, name),
        INDEX(category_group_id),
        FOREIGN KEY (ledger_id) REFERENCES ledgers(id) ON DELETE CASCADE,
        FOREIGN KEY (category_group_id) REFERENCES category_groups(id) ON DELETE CASCADE
    );

    -- 商店類型表
    CREATE TABLE merchant_types (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        code                             VARCHAR(50)   NOT NULL UNIQUE,                 -- restaurant、supermarket、utility
        name                             VARCHAR(100)  NOT NULL,                        -- 餐廳、超市、公共事業
        note                             VARCHAR(255),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL                             -- 刪除時間 (由後端寫入)
    );

    -- 交易商店表 
    CREATE TABLE merchants (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        country_id                       BIGINT        NULL,
        ledger_id                        BIGINT        NOT NULL,
        merchant_type_id                 BIGINT        NOT NULL,                        -- 商店 / 公司類型
        name                             VARCHAR(100)  NOT NULL,                        -- 商店 / 公司名稱
        created_date                     DATETIME      NOT NULL,	                    -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,	                    -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)
        UNIQUE(ledger_id, name),
        INDEX(ledger_id),
        INDEX(merchant_type_id),
        FOREIGN KEY (country_id) REFERENCES countries(id),
        FOREIGN KEY (ledger_id) REFERENCES ledgers(id) ON DELETE CASCADE,
        FOREIGN KEY (merchant_type_id) REFERENCES merchant_types(id)
    );

    -- 交易類型表
    CREATE TABLE transaction_types (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        ledger_id                        BIGINT        NOT NULL,
        code                             VARCHAR(30)   NOT NULL,	                    -- 代號
        name                             VARCHAR(50)   NOT NULL,                        -- 交易類型名稱 (如：
                                                                                        -- 收支活動 (income / expense / buy / sell)
                                                                                        -- 投資活動
                                                                                        -- 負債活動 (principal / interest / fee)
                                                                                        -- 應收帳款活動 (lend / receive / interest)
                                                                                        -- 固定資產活動 (depreciation / repair / revalue)
                                                                                        -- 存貨 (in / out / adjust)
        direction                        TINYINT       NOT NULL,                        -- 正向 / 負向 (income=1、expense=-1、buy=-1、sell=1、transfer=0)
        affects_balance                  BOOLEAN       DEFAULT TRUE,
        note                             VARCHAR(255),
        
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,		                -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)
        
        UNIQUE (ledger_id, code),
        FOREIGN KEY (ledger_id) REFERENCES ledgers(id) ON DELETE CASCADE
    );


    -- 交易表
    CREATE TABLE transactions (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        ledger_id                        BIGINT        NOT NULL,
        account_id                       BIGINT        NOT NULL,
        transaction_type_id              BIGINT        NOT NULL,
        
        price                            DECIMAL(18,8) NULL,                            -- 價格
        quantity                         DECIMAL(18,8) NULL,                            -- 數量
        amount                           DECIMAL(18,8) NOT NULL DEFAULT 0,              -- 合計 (實際影響帳戶餘額的金額)

        original_currency_id             BIGINT        NULL,                            -- 原始交易幣別 (外幣交易使用)
        original_amount                  DECIMAL(18,8) NULL,                            -- 原始交易金額 (外幣交易使用)
        exchange_rate_used               DECIMAL(18,8) NULL,                            -- 當下換算匯率 (外幣交易使用)

        transaction_date                 DATETIME      NOT NULL,                        -- 交易日
        note                             VARCHAR(255),

        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)
        
        INDEX (account_id, transaction_date),
        INDEX (ledger_id, transaction_date),
        INDEX (transaction_type_id),
        INDEX (transaction_date),
        
        FOREIGN KEY (ledger_id) REFERENCES ledgers(id),
        FOREIGN KEY (account_id) REFERENCES accounts(id),
        FOREIGN KEY (transaction_type_id) REFERENCES transaction_types(id),
        FOREIGN KEY (original_currency_id) REFERENCES currencies(id)
    );

    -- 交易關聯表 (不同交易之間的關聯)
    CREATE TABLE transaction_links (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,

        transaction_id                   BIGINT        NOT NULL,
        related_transaction_id           BIGINT        NOT NULL,

        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)

        UNIQUE(transaction_id, related_transaction_id),
        CHECK (transaction_id <> related_transaction_id),                               -- (後端應避免雙向連結)
        FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
        FOREIGN KEY (related_transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
    );

    -- 交易表子表 (收支)
    CREATE TABLE cashflow_transaction_details (
        transaction_id                   BIGINT        PRIMARY KEY,
        category_id                      BIGINT        NOT NULL,
        merchant_id                      BIGINT        NULL,
        INDEX(category_id),
        FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
        FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
        FOREIGN KEY (merchant_id) REFERENCES merchants(id) ON DELETE SET NULL
    );

    -- 交易表子表 (投資)
    CREATE TABLE investment_transaction_details (
        transaction_id                   BIGINT        PRIMARY KEY,
        investment_product_id            BIGINT        NOT NULL,
        fee                              DECIMAL(18,8) NOT NULL DEFAULT 0,               -- 手續費
        tax                              DECIMAL(18,8) NOT NULL DEFAULT 0,               -- 稅額
        FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
        FOREIGN KEY (investment_product_id) REFERENCES investment_products(id)
    );
    
    -- 交易表子表 (存貨)
    CREATE TABLE inventory_transaction_details (
        transaction_id                   BIGINT        PRIMARY KEY,
        expiry_date                      DATE,                                          -- 這批存貨的到期日
        FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
    );

    -- 重複交易表
    CREATE TABLE recurring_transactions (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        ledger_id                        BIGINT        NOT NULL,
        account_id                       BIGINT        NOT NULL,
        transaction_type_id              BIGINT        NOT NULL,

        price                            DECIMAL(18,8) NULL,                            -- 價格
        quantity                         DECIMAL(18,8) NULL,                            -- 數量
        amount                           DECIMAL(18,8) NOT NULL DEFAULT 0,              -- 合計 (實際影響帳戶餘額的金額)

        original_currency_id             BIGINT        NULL,                            -- 原始交易幣別 (外幣交易使用)
        original_amount                  DECIMAL(18,8) NULL,                            -- 原始交易金額 (外幣交易使用)
        exchange_rate_used               DECIMAL(18,8) NULL,                            -- 當下換算匯率 (外幣交易使用)

        recurrence_frequency_id          BIGINT        NOT NULL,                        -- 週期規則(如 一月一次、每日一次 等等，應由後端設定)
        start_date                       DATE          NOT NULL,
        end_date                         DATE          NULL,
        is_active                        BOOLEAN       NOT NULL DEFAULT TRUE,

        note                             VARCHAR(255),

        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)

        INDEX(start_date),
        INDEX(account_id),
        INDEX(ledger_id),
        INDEX(recurrence_frequency_id),
        
        FOREIGN KEY (ledger_id) REFERENCES ledgers(id),
        FOREIGN KEY (account_id) REFERENCES accounts(id),
        FOREIGN KEY (transaction_type_id) REFERENCES transaction_types(id),
        FOREIGN KEY (recurrence_frequency_id) REFERENCES frequencies(id),
        FOREIGN KEY (original_currency_id) REFERENCES currencies(id)
    );

    -- 重複交易關聯表 (不同交易之間的關聯)
    CREATE TABLE recurring_transaction_links (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,

        recurring_transaction_id         BIGINT        NOT NULL,
        related_recurring_transaction_id BIGINT        NOT NULL,

        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)

        UNIQUE(recurring_transaction_id, related_recurring_transaction_id),
        CHECK (recurring_transaction_id <> related_recurring_transaction_id),           -- (後端應避免雙向連結)
        FOREIGN KEY (recurring_transaction_id) REFERENCES recurring_transactions(id) ON DELETE CASCADE,
        FOREIGN KEY (related_recurring_transaction_id) REFERENCES recurring_transactions(id) ON DELETE CASCADE
    );

    -- 重複交易子表 (收支)
    CREATE TABLE cashflow_recurring_transaction_details (
        recurring_transaction_id         BIGINT        PRIMARY KEY,
        category_id                      BIGINT        NOT NULL,
        merchant_id                      BIGINT        NULL,
        FOREIGN KEY (recurring_transaction_id) REFERENCES recurring_transactions(id) ON DELETE CASCADE,
        FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
        FOREIGN KEY (merchant_id) REFERENCES merchants(id) ON DELETE SET NULL
    );
    
    -- 重複交易子表 (投資)
    CREATE TABLE investment_recurring_transaction_details (
        recurring_transaction_id         BIGINT        PRIMARY KEY,
        investment_product_id            BIGINT        NOT NULL,
        fee                              DECIMAL(18,8) NOT NULL DEFAULT 0,               -- 手續費
        tax                              DECIMAL(18,8) NOT NULL DEFAULT 0,               -- 稅額
        FOREIGN KEY (recurring_transaction_id) REFERENCES recurring_transactions(id) ON DELETE CASCADE,
        FOREIGN KEY (investment_product_id) REFERENCES investment_products(id)
    );

    -- 交易標籤表
    CREATE TABLE transaction_tags (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        ledger_id                        BIGINT        NOT NULL,
        name                             VARCHAR(50)   NOT NULL,
        color                            VARCHAR(20),
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)
        UNIQUE (ledger_id, name),
        FOREIGN KEY (ledger_id) REFERENCES ledgers(id) ON DELETE CASCADE
    );

    -- 交易標籤關聯表
    CREATE TABLE transaction_tag_links (
        transaction_id                   BIGINT        NOT NULL,
        tag_id                           BIGINT        NOT NULL,
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)
        PRIMARY KEY (transaction_id, tag_id),
        FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
        FOREIGN KEY (tag_id) REFERENCES transaction_tags(id) ON DELETE CASCADE
    );

    -- 重複交易標籤關聯表
    CREATE TABLE recurring_transaction_tag_links (
        recurring_transaction_id         BIGINT        NOT NULL,
        tag_id                           BIGINT        NOT NULL,
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)
        updated_date                     DATETIME      NOT NULL,                        -- 更新時間 (由後端寫入)
        deleted_date                     DATETIME      NULL,                            -- 刪除時間 (由後端寫入)
        PRIMARY KEY (recurring_transaction_id, tag_id),
        FOREIGN KEY (recurring_transaction_id) REFERENCES recurring_transactions(id) ON DELETE CASCADE,
        FOREIGN KEY (tag_id) REFERENCES transaction_tags(id) ON DELETE CASCADE
    );

    -- 帳戶調整表 (會計用)
    CREATE TABLE account_adjustments (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        account_id                       BIGINT        NOT NULL,

        before_balance                   DECIMAL(18,8) NOT NULL,                        -- 調整前餘額
        after_balance                    DECIMAL(18,8) NOT NULL,                        -- 調整後餘額

        reason                           VARCHAR(255)  NOT NULL,                        -- 調整原因
        note                             VARCHAR(255),

        created_by                       BIGINT        NOT NULL,                        -- 誰做的調整
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)

        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (created_by) REFERENCES users(id)
    );

    -- 帳戶餘額快照表
    CREATE TABLE account_balance_snapshots (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        account_id                       BIGINT        NOT NULL,
        
        balance                          DECIMAL(18,8) NOT NULL,
        currency_id                      BIGINT        NOT NULL,

        base_balance                     DECIMAL(18,8) NOT NULL,                        -- 折算本位幣價值
        base_currency_id                 BIGINT        NOT NULL,

        snapshot_date                    DATE            NOT NULL,                      -- 快照日期
        created_date                     DATETIME        NOT NULL,
    
        UNIQUE KEY uk_account_snapshot (account_id, snapshot_date),
        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (currency_id) REFERENCES currencies(id),
        FOREIGN KEY (base_currency_id) REFERENCES currencies(id)
    );

    -- 投資持倉快照表
    CREATE TABLE investment_position_snapshots (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        account_id                       BIGINT        NOT NULL,
        investment_product_id            BIGINT        NOT NULL,

        quantity                         DECIMAL(18,8) NOT NULL,                        -- 持有數量
        avg_cost                         DECIMAL(18,8) NOT NULL,                        -- 平均成本
        market_value                     DECIMAL(18,8) NULL,                            -- 市值 (快照時計算)

        snapshot_date                    DATE          NOT NULL,                        -- 快照日期
        created_date                     DATETIME      NOT NULL,                        -- 建立時間 (由後端寫入)

        UNIQUE (account_id, investment_product_id, snapshot_date),
        INDEX(account_id),

        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
        FOREIGN KEY (investment_product_id) REFERENCES investment_products(id)
    );

    -- 登入紀錄表
    CREATE TABLE login_history (
        id                               BIGINT        AUTO_INCREMENT PRIMARY KEY,
        user_id                          BIGINT        NULL,
        email                            VARCHAR(255)  NOT NULL,
        
        login_time                       DATETIME      NOT NULL,
        user_agent                       VARCHAR(255),
        ip_address                       VARCHAR(45),
        success                          BOOLEAN       NOT NULL DEFAULT FALSE,

        INDEX(user_id, login_time),

        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
    );



國別資料來源：https://github.com/dr5hn/countries-states-cities-database/tree/master
