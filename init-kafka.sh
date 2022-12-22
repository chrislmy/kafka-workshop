# Setup kafka environment
docker compose up -d

# Blocks until kafka is reachable
kafka-topics --bootstrap-server kafka:29092 --list

# Create topics
docker exec kafka kafka-topics --bootstrap-server kafka:29092 --create --topic travel_routes --replication-factor 1 --partitions 3

# Describe topics
docker exec kafka kafka-topics --bootstrap-server kafka:29092 --describe --topic travel_routes

