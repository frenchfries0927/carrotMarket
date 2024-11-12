 CREATE TABLE User (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     username VARCHAR(255) NOT NULL,
     password VARCHAR(255) NOT NULL,
     email VARCHAR(255) NOT NULL,
     location VARCHAR(255) COMMENT '사용자 주소 저장 (서울특별시 형태)',
     latitude DECIMAL(10, 7) COMMENT '위도',
     longitude DECIMAL(10, 7) COMMENT '경도',
     profileImage VARCHAR(255),
     createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     userGroup ENUM('ADMIN', 'GENERAL') NOT NULL COMMENT 'ADMIN/GENERAL 구분'
);

 CREATE TABLE ItemBoard ( id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          description TEXT,
                          price DECIMAL(15, 2) NOT NULL COMMENT '가격 정보',
                          category VARCHAR(255) NOT NULL COMMENT '임시 카테고리 필드',
                          status ENUM('AVAILABLE', 'RESERVED', 'SOLD') NOT NULL COMMENT '물품 상태',
                          createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, userId BIGINT NOT NULL,
                          itemImage VARCHAR(255) COMMENT '이미지 저장 경로',
                          FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE
);
INSERT INTO User (id, username, password, email, location, latitude, longitude, profileImage, userGroup)
VALUES
(1, 'user1', 'password1', 'user1@example.com', 'Seoul', 37.5665, 126.9780, 'https://www.lorempixel.com/200/200', 'GENERAL'),
(2, 'user2', 'password2', 'user2@example.com', 'Busan', 35.1796, 129.0756, 'https://www.lorempixel.com/200/200', 'GENERAL'),
(3, 'user3', 'password3', 'user3@example.com', 'Incheon', 37.4563, 126.7052, 'https://www.lorempixel.com/200/200', 'GENERAL'),
(4, 'user4', 'password4', 'user4@example.com', 'Daegu', 35.8714, 128.6014, 'https://www.lorempixel.com/200/200', 'ADMIN'),
(5, 'user5', 'password5', 'user5@example.com', 'Daejeon', 36.3504, 127.3845, 'https://www.lorempixel.com/200/200', 'GENERAL');
INSERT INTO ItemBoard (id, title, description, price, category, status, userId, itemImage)
VALUES
(1, 'Stylish Jacket', 'A warm and comfortable jacket for winter.', 49900.00, '의류', 'AVAILABLE', 1, '/itemImages/item1.jpg'),
(2, 'Smartphone', 'Latest model with great features.', 999000.00, '전자제품', 'AVAILABLE', 2, '/itemImages/item2.jpg'),
(3, 'Mountain Bike', 'Perfect bike for mountain adventures.', 250000.00, '레저', 'RESERVED', 3, '/itemImages/item3.jpg'),
(4, 'Antique Vase', 'A beautiful antique vase from the 18th century.', 135000.00, '가구/인테리어', 'SOLD', 4, '/itemImages/item4.jpg'),
(5, 'Gaming Console', 'Popular gaming console with two controllers.', 450000.00, '전자제품', 'AVAILABLE', 5, '/itemImages/item5.jpg');
