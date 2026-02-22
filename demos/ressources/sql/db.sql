create database "election_db";
\c election_db;

create user "election_db_manager" with password '0123456';


GRANT CONNECT ON DATABASE election_db TO "election_db_manager" ;
GRANT CREATE ON SCHEMA public TO "election_db_manager";
GRANT SELECT,INSERT,UPDATE,DELETE ON ALL TABLES IN SCHEMA public TO "election_db_manager";