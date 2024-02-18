-- Insert data into users table
INSERT INTO forum_system.users (first_name, last_name, username, password, email, is_admin, is_banned, photo_url)
VALUES
('John', 'Smith', 'johndoe', 'Password1', 'johnsmith@example.com', 1, 0, 'assets/users/male1.jpg'),
('Jane', 'Johnson', 'janejohnson', 'Password2', 'janejohnson@example.com', 0, 0, 'assets/users/female1.jpg'),
('Alice', 'Smith', 'alicesmith', 'Password', 'alicesmith@example.com', 0, 0, 'assets/users/female2.jpg'),
('Bobby', 'Johnson', 'bobjohnson', 'Password4', 'bobjohnson@example.com', 0, 0, 'assets/users/male2.jpg'),
('Charlie', 'Brown', 'charliebrown', 'Password5', 'charliebrown@example.com', 0, 0, 'assets/users/defaultUser.png'),
('David', 'Williams', 'davidwilliams', 'Password6', 'davidwilliams@example.com', 0, 0, 'assets/users/defaultUser.png'),
('Even', 'Jones', 'evejones', 'Password7', 'evejones@example.com', 0, 0, 'assets/users/defaultUser.png'),
('Frank', 'Miller', 'frankmiller', 'Password8', 'frankmiller@example.com', 0, 0, 'assets/users/defaultUser.png'),
('Grace', 'Davis', 'gracedavis', 'Password9', 'gracedavis@example.com', 0, 1, 'assets/users/defaultUser.png'),
('Harry', 'Wilson', 'harrywilson', 'Password10', 'harrywilson@example.com', 0, 1, 'assets/users/defaultUser.png');

-- Insert data into phone_numbers table
INSERT INTO forum_system.phone_numbers (phone_number, user_id)
VALUES
('1234567890', 1),
('2345678901', 2),
('3456789012', 3),
('4567890123', 4),
('5678901234', 5),
('6789012345', 6),
('7890123456', 7),
('8901234567', 8),
('9012345678', 9),
('0123456789', 10);

-- Insert data into posts table
INSERT INTO forum_system.posts (created_by_id, title, content, creation_date)
VALUES
(10, 'Fitness and nutrition', 'Discover the secrets to a healthier lifestyle! Share tips and tricks for staying fit and making nutritious choices. Let''s achieve our fitness goals together!', NOW()),
(9, 'Self-Actualization Journals', 'Embark on a journey of self-discovery with our self-actualization journals. Reflect on your goals, aspirations, and personal growth. Let''s inspire and support each other on this transformative path!', NOW()),
(8, 'Personal Development', 'Welcome to the Personal Development forum! Let''s explore strategies for self-improvement, goal-setting, time management, and overcoming obstacles. Together, let''s unleash our full potential and achieve personal growth!', NOW()),
(7, 'Business', 'Join the Business forum for expert insights, networking opportunities, and discussions on entrepreneurship, management, and industry trends. Let''s drive success together!', NOW()),
(6, 'Entrepreneurial skills', 'Unlock the keys to entrepreneurial success in our forum! Dive deep into essential skills like leadership, resilience, creativity, and strategic thinking. Let''s empower each other to thrive in the business world!', NOW()),
(5, 'Supplements', 'Explore the world of supplements with us! From vitamins to protein powders, discuss the latest research, recommendations, and personal experiences. Let''s optimize our health and wellness journeys together!', NOW()),
(4, 'Life Purpose', 'Discover your life''s purpose with us! Explore meaningful discussions, personal insights, and practical strategies to uncover and pursue your true calling. Let''s embark on this transformative journey together!', NOW()),
(3, 'Mindfulness', 'Embark on a journey of mindfulness with us! Explore practices, techniques, and experiences that promote present-moment awareness and inner peace. Let''s cultivate mindfulness together and live with greater clarity and intention.', NOW()),
(2, 'Career', 'Navigate your career path with confidence in our forum! From job hunting tips to professional development strategies, let''s share insights and empower each other to thrive in the ever-evolving world of work.', NOW()),
(1, 'Relationships', 'Explore the complexities of relationships in our forum! From romantic partnerships to friendships and family dynamics, let''s share insights, offer support, and foster healthy connections. Together, let''s navigate the beautiful tapestry of human relationships.', NOW());

-- Insert data into comments table
INSERT INTO forum_system.comments (post_id, created_by_id, content, creation_date, isDeleted)
VALUES
(1, 1, 'I have been struggling with my diet lately. Any advice on sticking to healthy eating habits?', NOW(), false),
(1, 2, 'Exercise has been my stress reliever lately. Nothing beats a good workout session!', NOW(), false),
(1, 3, 'Meal prepping has been a game-changer for me. It helps me stay on track with my nutrition goals!', NOW(), false),
(2, 4, 'I have found journaling to be incredibly therapeutic. It iss amazing how writing down our thoughts can lead to profound insights and self-awareness.', NOW(), false),
(2, 5, 'I am excited to start my journaling journey here! Sharing experiences and reflections with like-minded individuals feels empowering.', NOW(), false),
(3, 6, 'I have been working on setting SMART goals lately, and it''s made a huge difference in my productivity and focus. Excited to learn more from this community!', NOW(), false),
(3, 7, 'Time management has always been a struggle for me. Any tips on how to prioritize tasks and avoid procrastination?', NOW(), false),
(3, 8, 'Personal development is a lifelong journey. I''m grateful for forums like this where we can share experiences and support each other''s growth.', NOW(), false),
(4, 8, 'Business acumen is key in today''s competitive landscape. Let''s exchange insights on market analysis, strategic planning, and customer engagement to drive success in our ventures', NOW(), false),
(5, 9, 'Master the art of entrepreneurship with us! From effective communication to financial literacy, let''s sharpen our skills and conquer the business world together.', NOW(), false),
(5, 10, 'Calling all aspiring entrepreneurs! Join our forum to learn the ropes of entrepreneurship. Explore topics like problem-solving, adaptability, and risk-taking. Let''s turn ideas into thriving ventures!', NOW(), false),
(6, 1, 'Delve into the realm of supplements with our forum! From natural remedies to cutting-edge supplements, let''s navigate the science, benefits, and potential risks together. Empower your journey to optimal health!', NOW(), false),
(7, 2, 'Finding one''s life purpose can be a profound journey of self-discovery. I''m grateful for spaces like this where we can share our experiences and support each other along the way.', NOW(), false),
(7, 3, 'I''ve been feeling lost lately, unsure of what I''m meant to do with my life. Excited to engage in discussions here and hopefully gain some clarity and inspiration.', NOW(), false),
(8, 4, 'Mindfulness has truly transformed my life, helping me to navigate stress and find moments of peace in the chaos. Grateful for communities like this that support and encourage this practice.', NOW(), false),
(8, 5, 'I''ve been wanting to incorporate mindfulness into my daily routine but struggle to find the time. Any tips or resources for beginners would be greatly appreciated!', NOW(), false),
(9, 4, 'Career advancement requires continuous learning and networking. Excited to engage in discussions here to stay updated on industry trends and exchange career growth strategies.', NOW(), false),
(9, 3, 'Navigating career transitions can be daunting. Grateful for this community where we can seek advice, share experiences, and support each other through our professional journeys.', NOW(), false),
(10, 2, 'Relationships are the cornerstone of our lives, yet they can be challenging to navigate. Grateful for this space where we can seek advice, share stories, and learn from each other''s experiences.', NOW(), false),
(10, 1, 'Communication is key in any relationship. I''ve found that actively listening and expressing myself openly has strengthened my connections with others. Looking forward to discussing effective communication strategies here.', NOW(), false),
(10, 10, 'Maintaining healthy boundaries is crucial for fostering positive relationships. Excited to explore topics like boundary-setting, self-care, and conflict resolution in this community.', NOW(), false);

-- Insert data into tags table
INSERT INTO forum_system.tags (tag_name)
VALUES
('Fitness'),
('Nutrition'),
('Self-Actualization'),
('Personal Development'),
('Business'),
('Entrepreneurship'),
('Supplements'),
('Life Purpose'),
('Mindfulness'),
('Career'),
('Relationships'),
('Technology'),
('Programming'),
('Art'),
('Music'),
('Travel'),
('Education'),
('Health'),
('Science'),
('Politics');

-- Insert data into posts_tags table
INSERT INTO forum_system.posts_tags (post_id, tag_id)
VALUES
(1, 1),
(1, 2),
(2, 3),
(3, 4),
(4, 5),
(5, 6),
(6, 7),
(7, 8),
(8, 9),
(9, 10),
(10, 11),
(1, 12),
(2, 13),
(3, 14),
(4, 15),
(5, 16),
(6, 17),
(7, 18),
(8, 19),
(9, 20);

-- Insert data into liked_comments table
INSERT INTO forum_system.liked_comments (user_id, comment_id, isLiked)
VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1),
(5, 5, 1),
(6, 6, 1),
(7, 7, 1),
(8, 8, 1),
(9, 9, 1),
(10, 10, 1);

-- Insert data into liked_posts table
INSERT INTO forum_system.liked_posts (post_id, user_id)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10);

-- Insert data into users_comments table
INSERT INTO forum_system.users_comments (user_id, comment_id)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10);

-- Insert data into users_posts table
INSERT INTO forum_system.users_posts (user_id, post_id)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10);

-- Insert data into users_comments table
INSERT INTO forum_system.users_comments (user_id, comment_id)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10);