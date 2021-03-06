create table if not exists education_levels (
  id bigint not null,
  value varchar(50) not null,
  primary key (id)
);

create table if not exists employments (
  id bigint not null,
  value varchar(100) not null,
  primary key (id)
);

create table if not exists fuels (
  id bigint not null,
  value varchar(50) not null,
  primary key (id)
);

create table if not exists car_sharing_services (
  id bigint not null,
  name varchar(50) not null,
  primary key (id)
);

create table if not exists travel_documents (
  id bigint not null,
  name varchar(50) not null,
  cost real not null,
  description varchar(1000),
  primary key (id)
);

create table if not exists status (
  id bigint not null,
  value varchar(50) not null,
  primary key (id)
);

create table if not exists users (
  id BIGSERIAL,
  nickname varchar(30) not null,
  email varchar(200) not null unique,
  password varchar(200) not null,
  status_id bigint DEFAULT 4, -- new users don't have yet completed the verification of email
  primary key (id),
  foreign key (status_id) references status(id)
);

create table if not exists topics(
  id bigint not null,
  value varchar(50) not null,
  description varchar(200) not null,
  primary key (id)
);


create table if not exists images (
  id BIGSERIAL not null,
  value BYTEA not null,
  primary key (id)
);

create table if not exists user_profiles (
  id bigint not null,
  sex varchar(1),
  date_of_birth DATE,
  education_level_id bigint,
  employment_id bigint,
  private_car_ownership boolean,
  car_registration_year integer,
  fuel_id bigint,
  car_sharing_usage boolean,
  car_sharing_service_id bigint,
  bike_usage boolean,
  private_bike_usage boolean,
  bike_sharing_usage boolean,
  public_transport_usage boolean,
  habitual_travel_document_id bigint,
  image_id bigint,
  primary key (id),
  foreign key (id) references users(id),
  foreign key (education_level_id) references education_levels(id),
  foreign key (employment_id) references employments(id),
  foreign key (fuel_id) references fuels(id),
  foreign key (car_sharing_service_id) references car_sharing_services(id),
  foreign key (habitual_travel_document_id) references travel_documents(id),
  foreign key (image_id) references images(id)
);

create table if not exists alert_types (
  id bigint not null,
  name varchar(30) not null,
  primary key (id)
);

create table if not exists alerts (
  id BIGSERIAL not null,
  alert_type_id bigint not null,
  user_id bigint not null,
  hashtag varchar(200) not null,
  rating real,
  address varchar(200) not null,
  lat double precision not null,
  lng double precision not null,
  activation_date TIMESTAMP not null,
  last_view_time TIMESTAMP not null,
  comment varchar(2000),
  primary key (id),
  foreign key (alert_type_id) references alert_types(id), -- Warning has a type
  foreign key (user_id) references users(id) -- User creates a warning
);

create table if not exists ratings (
	user_id bigint not null,
	alert_id bigint not null,
	vote integer not null, -- from 1.0 to 5.0
	primary key (user_id, alert_id),
	foreign key (user_id) references users(id),
	foreign key (alert_id) references alerts(id)
);

create table if not exists messages (
  id BIGSERIAL,
  sender_id bigint not null,
  text text not null,
  sending_time timestamp not null,
  topic_id bigint not null,
  image_id bigint,
  alert_id bigint,
  primary key (id),
  foreign key (sender_id) references users(id),
  foreign key (topic_id) references topics(id),
  foreign key (image_id) references images(id),
  foreign key (alert_id) references alerts(id)
);

create table if not exists verification_tokens (
  id bigint not null,
  token text not null,
  expiration timestamp not null,
  primary key (id),
  foreign key (id) references users(id)
);

CREATE OR REPLACE FUNCTION update_average_rating()
  RETURNS trigger AS
$BODY$
BEGIN
  UPDATE alerts
  SET rating = (SELECT avg(vote)
    FROM ratings
    WHERE alert_id = NEW.alert_id)
  WHERE id = NEW.alert_id;

  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

create trigger update_average_rating_trigger AFTER insert OR update ON ratings
FOR EACH ROW
EXECUTE PROCEDURE update_average_rating();