-- Create User table
CREATE TABLE User (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    social_type VARCHAR(50),
    social_id VARCHAR(255),
    user_type VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    registration_number VARCHAR(20) NOT NULL,
    account_number VARCHAR(50),
    bank_name VARCHAR(100),
    status VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- Create Bid table
CREATE TABLE Bid (
    bid_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    advertiser_id BIGINT NOT NULL,
    place_link VARCHAR(500) NOT NULL,
    desired_rank VARCHAR(50) NOT NULL,
    keyword VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    expires_at DATETIME NOT NULL,
    selected_agency_id BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (advertiser_id) REFERENCES User(user_id),
    FOREIGN KEY (selected_agency_id) REFERENCES User(user_id)
);

-- Create Proposal table
CREATE TABLE Proposal (
    proposal_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bid_id BIGINT NOT NULL,
    agency_id BIGINT NOT NULL,
    proposed_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (bid_id) REFERENCES Bid(bid_id),
    FOREIGN KEY (agency_id) REFERENCES User(user_id)
);

-- Create Payment table
CREATE TABLE Payment (
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bid_id BIGINT NOT NULL,
    advertiser_id BIGINT NOT NULL,
    agency_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    platform_fee DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    paid_at DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (bid_id) REFERENCES Bid(bid_id),
    FOREIGN KEY (advertiser_id) REFERENCES User(user_id),
    FOREIGN KEY (agency_id) REFERENCES User(user_id)
);

-- Create Settlement table
CREATE TABLE Settlement (
    settlement_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_id BIGINT NOT NULL,
    agency_id BIGINT NOT NULL,
    settlement_amount DECIMAL(10, 2) NOT NULL,
    proof_screenshot_url VARCHAR(500),
    status VARCHAR(50) NOT NULL,
    settled_at DATETIME,
    admin_checked_at DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (payment_id) REFERENCES Payment(payment_id),
    FOREIGN KEY (agency_id) REFERENCES User(user_id)
);

-- Create Review table
CREATE TABLE Review (
    review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    advertiser_id BIGINT NOT NULL,
    agency_id BIGINT NOT NULL,
    bid_id BIGINT,
    rating INT NOT NULL,
    comment TEXT,
    status VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (advertiser_id) REFERENCES User(user_id),
    FOREIGN KEY (agency_id) REFERENCES User(user_id),
    FOREIGN KEY (bid_id) REFERENCES Bid(bid_id)
);

-- Create Portfolio table
CREATE TABLE Portfolio (
    portfolio_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    agency_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    file_url VARCHAR(500) NOT NULL,
    status VARCHAR(50) NOT NULL,
    admin_memo TEXT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (agency_id) REFERENCES User(user_id)
);

-- Create Notification table
CREATE TABLE Notification (
    notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    sent_at DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- Create ChatRoom table
CREATE TABLE ChatRoom (
    chat_room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bid_id BIGINT NOT NULL,
    advertiser_id BIGINT NOT NULL,
    agency_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    FOREIGN KEY (bid_id) REFERENCES Bid(bid_id),
    FOREIGN KEY (advertiser_id) REFERENCES User(user_id),
    FOREIGN KEY (agency_id) REFERENCES User(user_id)
);

-- Create ChatMessage table
CREATE TABLE ChatMessage (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chat_room_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    message_content TEXT NOT NULL,
    file_url VARCHAR(500),
    sent_at DATETIME NOT NULL,
    FOREIGN KEY (chat_room_id) REFERENCES ChatRoom(chat_room_id),
    FOREIGN KEY (sender_id) REFERENCES User(user_id)
); 