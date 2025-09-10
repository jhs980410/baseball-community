import csv
import random

roles = ["user", "admin", "moderator"]
statuses = ["active", "suspended", "deleted"]

with open("users.csv", "w", newline="", encoding="utf-8") as f:
    writer = csv.writer(f)
    writer.writerow(["id", "email", "password", "nickname", "role", "status"])

    for i in range(1, 2001):  # 2000명 생성
        email = f"user{i}@test.com"
        password = f"pass{i:04d}"
        nickname = f"nick{i}"
        role = random.choice(roles)
        status = random.choice(statuses)
        writer.writerow([i, email, password, nickname, role, status])

print("✅ users.csv 생성 완료")
