import sqlite3
import os.path

currentPath = os.path.dirname(os.path.abspath(__file__))
dbPath = currentPath.split("mirrorRendering")[0] + "SMWS.db"
conn = sqlite3.connect(dbPath)
c = conn.cursor()

results = c.execute('select * from Message;')

for result in results:
    print(result)