INSERT INTO ROLES(ROLE_VALUE)
VALUES ('ADMIN');

INSERT INTO ROLES(ROLE_VALUE)
VALUES ('MANAGER');

INSERT INTO ROLES(ROLE_VALUE)
VALUES ('EMPLOYEE');

INSERT INTO MANAGERS(EMAIL)
VALUES ('admin@admin.com');

INSERT INTO MANAGERS(EMAIL)
VALUES ('silviu.butnaru@student.tuiasi.ro');

INSERT INTO MANAGERS(EMAIL)
VALUES ('alexandra.stefan@student.tuiasi.ro');

INSERT INTO MANAGERS(EMAIL)
VALUES ('alberto-ionut.toscariu@student.tuiasi.ro');

-- pass: admin
INSERT INTO EMPLOYEES(DEPARTMENT, EMAIL, EMPLOYEE_NUMBER, FIRST_NAME, LAST_NAME, ID_MANAGER, PASSWORD, JOIN_DATE, IMAGE)
VALUES ('ADMIN', 'admin@admin.com', 0, 'admin', 'admin', 1,
        '$2a$10$LT7B3Tc47NuzcpZDEvPt5.ctHrHbM/AECWTgn53EgVOkHwNy6kV4C', CURRENT_TIMESTAMP,
        LO_IMPORT('/docker-entrypoint-initdb.d/admin-icon.png'));

-- pass: silviu
INSERT INTO EMPLOYEES(DEPARTMENT, EMAIL, EMPLOYEE_NUMBER, FIRST_NAME, LAST_NAME, ID_MANAGER, PASSWORD, JOIN_DATE, IMAGE)
VALUES ('VNI & HMI', 'silviu.butnaru@student.tuiasi.ro', 1, 'Silviu', 'Butnaru', 1,
        '$2a$10$8ZDaoPmScOgEclTEUMh/Ku6njvMX3ae.qiQKNax9YKPsd7rqa2irO', CURRENT_TIMESTAMP,
        LO_IMPORT('/docker-entrypoint-initdb.d/user-icon.png'));

-- pass: alexandra
INSERT INTO EMPLOYEES(DEPARTMENT, EMAIL, EMPLOYEE_NUMBER, FIRST_NAME, LAST_NAME, ID_MANAGER, PASSWORD, JOIN_DATE, IMAGE)
VALUES ('HMI', 'alexandra.stefan@student.tuiasi.ro', 2, 'Alexandra', 'Stefan', 2,
        '$2a$10$LfggPragOiLHiLBBf3wSAe6//absugtgc86Cxr9Wl.mMG/RaXY19C', CURRENT_TIMESTAMP,
        LO_IMPORT('/docker-entrypoint-initdb.d/user-icon.png'));

-- pass: alberto-ionut
INSERT INTO EMPLOYEES(DEPARTMENT, EMAIL, EMPLOYEE_NUMBER, FIRST_NAME, LAST_NAME, ID_MANAGER, PASSWORD, JOIN_DATE, IMAGE)
VALUES ('VNI', 'alberto-ionut.toscariu@student.tuiasi.ro', 3, 'Alberto-Ionut', 'Toscariu', 2,
        '$2a$10$PGIKEzrA35U077fFS8H1hu30ZOXGZCTrKtmpWu68hQKOJ0ehnpVO6', CURRENT_TIMESTAMP,
        LO_IMPORT('/docker-entrypoint-initdb.d/user-icon.png'));

-- pass: andrei
INSERT INTO EMPLOYEES(DEPARTMENT, EMAIL, EMPLOYEE_NUMBER, FIRST_NAME, LAST_NAME, ID_MANAGER, PASSWORD, JOIN_DATE, IMAGE)
VALUES ('HMI', 'andrei.petcu@student.tuiasi.ro', 4, 'Andrei', 'Petcu', 3,
        '$2a$10$D6rLx4u1tiSzbfJWC5kv4e6BoN34/AuWAGeYhpEqdBKfNOG9wzMO2', CURRENT_TIMESTAMP,
        LO_IMPORT('/docker-entrypoint-initdb.d/user-icon.png'));

-- pass: dan-claudiu
INSERT INTO EMPLOYEES(DEPARTMENT, EMAIL, EMPLOYEE_NUMBER, FIRST_NAME, LAST_NAME, ID_MANAGER, PASSWORD, JOIN_DATE, IMAGE)
VALUES ('HMI', 'dan-claudiu.taga@student.tuiasi.ro', 5, 'Dan-Claudiu', 'Taga', 3,
        '$2a$10$m8sYIzF5QKLDb/pxnTZD4uoDGSXo1VVYA7.UnZ0SLzfIwpWykib0e', CURRENT_TIMESTAMP,
        LO_IMPORT('/docker-entrypoint-initdb.d/user-icon.png'));

-- pass: laurentiu-stefan
INSERT INTO EMPLOYEES(DEPARTMENT, EMAIL, EMPLOYEE_NUMBER, FIRST_NAME, LAST_NAME, ID_MANAGER, PASSWORD, JOIN_DATE, IMAGE)
VALUES ('VNI', 'laurentiu-stefan.balauta@student.tuiasi.ro', 6, 'Laurentiu-Stefan', 'Balauta', 4,
        '$2a$10$OtbNdQlqR.oYoKh8xoNJh.xeXSprUDn3Bpp9HYH21b67oZKuQ9Yzi', CURRENT_TIMESTAMP,
        LO_IMPORT('/docker-entrypoint-initdb.d/user-icon.png'));

-- pass: mihai-adrian
INSERT INTO EMPLOYEES(DEPARTMENT, EMAIL, EMPLOYEE_NUMBER, FIRST_NAME, LAST_NAME, ID_MANAGER, PASSWORD, JOIN_DATE, IMAGE)
VALUES ('VNI', 'mihai-adrian.mircea@student.tuiasi.ro', 7, 'Mihai-Adrian', 'Mircea', 4,
        '$2a$10$SZSFkqe5gnQ8da3aU/VCB.rXihpaYE.y0Hk8zOWbOdeduOqWiktdm', CURRENT_TIMESTAMP,
        LO_IMPORT('/docker-entrypoint-initdb.d/user-icon.png'));

-- pass: rares-petru
INSERT INTO EMPLOYEES(DEPARTMENT, EMAIL, EMPLOYEE_NUMBER, FIRST_NAME, LAST_NAME, ID_MANAGER, PASSWORD, JOIN_DATE, IMAGE)
VALUES ('VNI', 'rares-petru.vanga@student.tuiasi.ro', 8, 'Rares-Petru', 'Vanga', 4,
        '$2a$10$TepbieCCQVS0EvxR3z/1luay4orkFprwoQFQeIlCDLxU9V3f8zhZC', CURRENT_TIMESTAMP,
        LO_IMPORT('/docker-entrypoint-initdb.d/user-icon.png'));

-- admin -> ADMIN
INSERT INTO EMPLOYEES_ROLES(ID_EMPLOYEE, ID_ROLE)
VALUES (1, 1);

-- silviu -> MANAGER
INSERT INTO EMPLOYEES_ROLES(ID_EMPLOYEE, ID_ROLE)
VALUES (2, 2);

-- alexandra -> MANAGER, EMPLOYEE
INSERT INTO EMPLOYEES_ROLES(ID_EMPLOYEE, ID_ROLE)
VALUES (3, 2);
INSERT INTO EMPLOYEES_ROLES(ID_EMPLOYEE, ID_ROLE)
VALUES (3, 3);

-- alberto-ionut -> MANAGER, EMPLOYEE
INSERT INTO EMPLOYEES_ROLES(ID_EMPLOYEE, ID_ROLE)
VALUES (4, 2);
INSERT INTO EMPLOYEES_ROLES(ID_EMPLOYEE, ID_ROLE)
VALUES (4, 3);

-- andrei: EMPLOYEE
INSERT INTO EMPLOYEES_ROLES(ID_EMPLOYEE, ID_ROLE)
VALUES (5, 3);

-- dan-claudiu: EMPLOYEE
INSERT INTO EMPLOYEES_ROLES(ID_EMPLOYEE, ID_ROLE)
VALUES (6, 3);

-- laurentiu-stefan: EMPLOYEE
INSERT INTO EMPLOYEES_ROLES(ID_EMPLOYEE, ID_ROLE)
VALUES (7, 3);

-- mihai-adrian: EMPLOYEE
INSERT INTO EMPLOYEES_ROLES(ID_EMPLOYEE, ID_ROLE)
VALUES (8, 3);

-- rares-petru: EMPLOYEE
INSERT INTO EMPLOYEES_ROLES(ID_EMPLOYEE, ID_ROLE)
VALUES (9, 3);