CREATE TABLE candidate ( 
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL 
);

CREATE TABLE voter ( 
    id SERIAL PRIMARY KEY, 
    name TEXT NOT NULL 
);

CREATE TYPE vote_type AS ENUM('VALID', 'BLANK', 'NULL');

CREATE TABLE vote (
    id SERIAL PRIMARY KEY,
    candidate_id INT REFERENCES candidate (id),
    voter_id INT NOT NULL REFERENCES voter (id),
    vote_type vote_type NOT NULL
);

select count(id) as total_votes from vote;

select vote_type , count(id)  as nombre_votes from vote group by vote_type
order by case vote_type when 'VALID' then 1 when 'BLANK' then 2
when 'NULL' then 3 end;

select c.name as candidate_name, count(v.id) as valid_count from candidate c
left join vote v on c.id = v.candidate_id and v.vote_type = 'VALID'
group by c.name order by c.name;

select sum(case when vote_type = 'VALID' then 1 else 0 end) as valid_count,
sum(case when vote_type = 'BLANK' then 1 else 0 end) as blank_count,
sum(case when vote_type = 'NULL' then 1 else 0 end) as null_count
from vote;

select 
    (select count(id) from vote) as votes_count,
    (select count(id) from voter) as total_voters;