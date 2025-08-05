#!/bin/bash

# Test script for the Item API
API_URL="http://localhost:8080"

echo "🧪 Testing Quarkus Item API"
echo "=============================="

echo ""
echo "1. 📋 Getting all items (should be empty initially):"
curl -s -X GET "$API_URL/items" | jq '.' || curl -s -X GET "$API_URL/items"

echo ""
echo ""
echo "2. ➕ Creating a new item:"
ITEM_1=$(curl -s -X POST "$API_URL/items" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Item 1"}')
echo "$ITEM_1" | jq '.' || echo "$ITEM_1"

echo ""
echo ""
echo "3. ➕ Creating another item:"
ITEM_2=$(curl -s -X POST "$API_URL/items" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Item 2"}')
echo "$ITEM_2" | jq '.' || echo "$ITEM_2"

echo ""
echo ""
echo "4. 📋 Getting all items (should show 2 items now):"
curl -s -X GET "$API_URL/items" | jq '.' || curl -s -X GET "$API_URL/items"

echo ""
echo ""
echo "5. 🔍 Getting item by ID (using ID 1):"
curl -s -X GET "$API_URL/items/1" | jq '.' || curl -s -X GET "$API_URL/items/1"

echo ""
echo ""
echo "6. 🗑️  Deleting item with ID 1:"
curl -s -X DELETE "$API_URL/items/1" -w "HTTP Status: %{http_code}\n"

echo ""
echo ""
echo "7. 📋 Getting all items (should show only 1 item now):"
curl -s -X GET "$API_URL/items" | jq '.' || curl -s -X GET "$API_URL/items"

echo ""
echo ""
echo "8. 🔍 Trying to get deleted item (should return null or 404):"
curl -s -X GET "$API_URL/items/1" -w "HTTP Status: %{http_code}\n" | jq '.' || curl -s -X GET "$API_URL/items/1" -w "HTTP Status: %{http_code}\n"

echo ""
echo ""
echo "✅ API testing completed!"
