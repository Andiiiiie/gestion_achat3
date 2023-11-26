INSERT INTO public.service (id, name) VALUES (1, 'test');
INSERT INTO public.service (id, name) VALUES (2, 'ACHAT');
INSERT INTO public.service (id, name) VALUES (3, 'FINANCE');
INSERT INTO public.service (id, name) VALUES (4, 'DAF');
INSERT INTO public.service (id, name) VALUES (5, 'DG');


INSERT INTO public.request_type (id, designation) VALUES (1, 'ilaina');


INSERT INTO public.product (id, designation, tva) VALUES (1, 'chaise', 20);
INSERT INTO public.product (id, designation, tva) VALUES (2, 'stylo', 20);
INSERT INTO public.product (id, designation, tva) VALUES (3, 'table', 20);
INSERT INTO public.product (id, designation, tva) VALUES (4, 'tele', 20);


INSERT INTO public.user_table (id, address, email, first_name, last_name, password, phone_number, roles, service_id) VALUES (1, 'tany', 'test@test.com', 'Jessica', 'Andie', '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.', '034 60 300 34', 'USER,CHEF', 1);
INSERT INTO public.user_table (id, address, email, first_name, last_name, password, phone_number, roles, service_id) VALUES (2, 'tana', 'achat@test.com', 'Rakoto', 'Be', '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.', '033 60 300 34', 'USER', 2);
INSERT INTO public.user_table (id, address, email, first_name, last_name, password, phone_number, roles, service_id) VALUES (5, 'tana', 'daf@test.com', 'Jean2', 'Jean2', '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.', '033 60 300 34', 'USER', 4);
INSERT INTO public.user_table (id, address, email, first_name, last_name, password, phone_number, roles, service_id) VALUES (4, 'tana', 'dg@test.com', 'Jean3', 'Jean3', '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.', '033 60 300 34', 'USER', 5);
INSERT INTO public.user_table (id, address, email, first_name, last_name, password, phone_number, roles, service_id) VALUES (3, 'tana', 'finance@test.com', 'Jean1', 'Jean1', '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.', '033 60 300 34', 'USER,CHEF', 3);


INSERT INTO public.supplier (id, adresse, contact, name, responsable) VALUES (1, 'mail@mail.com', '034 33 422 42', 'JEAN', 'JEAN');
INSERT INTO public.supplier (id, adresse, contact, name, responsable) VALUES (2, 'mail@mail.com', '033 33 422 42', 'GLOSSISTE', 'JEAN');
INSERT INTO public.supplier (id, adresse, contact, name, responsable) VALUES (3, 'mail@mail.com', '032 33 422 42', 'RABE', 'RABE');
