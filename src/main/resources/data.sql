-- Pizza Data
INSERT INTO pizza (name, description, price, size, available, created_at, updated_at)
VALUES
('Margherita', 'Pizza clássica com molho de tomate, mozzarella e manjericão fresco', 45.00, 'MEDIUM', true, NOW(), NOW()),
('Pepperoni', 'Pizza com generosa quantidade de pepperoni e queijo derretido', 50.00, 'MEDIUM', true, NOW(), NOW()),
('Quatro Queijos', 'Uma combinação deliciosa de 4 tipos diferentes de queijo', 55.00, 'LARGE', true, NOW(), NOW()),
('Vegetariana', 'Recheada com legumes frescos: tomate, cebola, pimentão, cogumelo e azeitona', 40.00, 'MEDIUM', true, NOW(), NOW()),
('Havaiana', 'Pizza com presunto, abacaxi e queijo', 48.00, 'MEDIUM', true, NOW(), NOW()),
('Carnívora', 'Perfeita para quem ama carne: calabresa, bacon, frango e presunto', 60.00, 'LARGE', true, NOW(), NOW()),
('Frango com Catupiry', 'Frango desfiado com molho de catupiry cremoso', 52.00, 'MEDIUM', true, NOW(), NOW()),
('Portuguesa', 'Presunto, ovo, azeitona, cebola, pimentão e tomate', 48.00, 'MEDIUM', true, NOW(), NOW());

-- Customer Data for Testing
INSERT INTO customer (id, name, email, phone, address, zip_code, city, state, created_at, updated_at)
VALUES
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01', 'João Silva', 'joao@example.com', '11999999999', 'Rua Teste, 123', '01234-567', 'São Paulo', 'SP', NOW(), NOW()),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b02', 'Maria Santos', 'maria@example.com', '11988888888', 'Av. Principal, 456', '02345-678', 'São Paulo', 'SP', NOW(), NOW());

