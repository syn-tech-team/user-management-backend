
INSERT INTO address (id, user_id, address_line1, address_line2, city, state, zipCode, country, type, primary_address)
VALUE(1, 1, '123 Main St', '', 'Houston', 'TX', '77001', 'US', 'home', TRUE),
    (2,1, '500 Corporate Dr', 'Floor 2', 'Houston', 'TX', '77002', 'US', 'office',FALSE);

INSERT INTO address (id, user_id, address_line1, address_line2, city, state, zipCode, country, type, primary_address)
VALUES (3,2, '789 Park Ave', 'Suite 300','Austin', 'TX', '73301', 'US', 'home', TRUE);
