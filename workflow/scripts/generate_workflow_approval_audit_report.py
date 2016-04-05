#!/usr/bin/python

# import the MySQLdb and sys modules.
import MySQLdb
import sys
import csv

# open a database connection.
connection = MySQLdb.connect(host = "localhost", user = "root", passwd = "root" , db = "dialg_stats")
cursor = connection.cursor()

# [START] Generating 'app_approval_audit_report.csv'.

print "[START] Generating 'app_approval_audit_report.csv'..."

cursor.execute("SELECT * FROM APP_APPROVAL_AUDIT")
app_data = cursor.fetchall()

app_file = open("app_approval_audit_report.csv", "wb")
c = csv.writer(app_file)
c.writerow(["Application Name","Application Creator","Approval Status","Approval Type","Completed By Role","Completed By User","Completed On"])

for row in app_data :
    c.writerow([row[0],row[1],row[2],row[3],row[4],row[5],row[6]])

app_file.close()

print "[END] Completed generating 'app_approval_audit_report.csv'..."

# [START] Generating 'sub_approval_audit_report.csv'.

print "[START] Generating 'sub_approval_audit_report.csv'..."

cursor.execute("SELECT * FROM SUB_APPROVAL_AUDIT")
sub_data = cursor.fetchall()

sub_file = open("sub_approval_audit_report.csv", "wb")
c = csv.writer(sub_file)
c.writerow(["API Name","API Version","API Provider","Approval Status","Approval Type","Completed By Role","Completed By User","Completed On"])

for row in sub_data :
    c.writerow([row[1],row[2],row[0],row[4],row[5],row[6],row[7],row[8]])

sub_file.close()

print "[END] Completed generating 'sub_approval_audit_report.csv'..."

# close the cursor/connection objects.
cursor.close ()
connection.close ()

sys.exit()
