insert into chat.user(login, password) values
    ('John', 'johnOriginal12!'),
    ('Mike', 'oMikeGod666'),
    ('Jo' , 'joJo123'),
    ('Phillip', 'alePhillip777'),
    ('Karl', 'Gallager0703Karl');

insert into chat.chatroom(name, owner) values
    ('school21', 1),
    ('rocket', 2),
    ('BLACKPINK', 3),
    ('readers', 4),
    ('police', 5);

insert into chat.message(author, room, text, dateTime) values
    (1, 1, 'Who is at school today?', current_timestamp),
    (2, 1, 'I will be there around two oclock in the afternoon', current_timestamp),
    (3, 1, 'And I will not come', current_timestamp),
    (2, 2, 'Rocketchat is worse than slack!', current_timestamp),
    (1, 2, 'I support!', current_timestamp),
    (3, 3, 'I love all the members of BP!!!', current_timestamp),
    (1, 3, 'I love Jisoo!', current_timestamp),
    (4, 3, 'And I am Lisa! Jisoo and Lisa are my favorite pairing!', current_timestamp),
    (2, 3, 'Rose! Rose! Rose is the best!', current_timestamp),
    (5, 3, 'Jenny is beautiful and talented :*', current_timestamp),
    (4, 4, 'Who has read Nekrasov is Two Captains?', current_timestamp),
    (5, 5, 'I am the best cop and Gallagher', current_timestamp);
