import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
import sys

# Arguments du script : build_number, status, test_summary, jenkins_url
build_number = sys.argv[1]
status = sys.argv[2]
test_summary = sys.argv[3]
jenkins_url = sys.argv[4]

sender_email = "braininformatiqueservice@gmail.com"
receiver_email = "malloukiyoussef22@gmail.com"
password = "kcefeysilehngbxw"

message = MIMEMultipart("alternative")
message["Subject"] = f"✅ Rapport pipeline build #{build_number} - {status}"
message["From"] = sender_email
message["To"] = receiver_email

html = f"""
<html>
  <body>
    <h2>Résultat du pipeline</h2>
    <ul>
      <li><b>Build :</b> #{build_number}</li>
      <li><b>Statut :</b> {status}</li>
      <li><b>Tests :</b> {test_summary}</li>
      <li><b>Lien Jenkins :</b> <a href="{jenkins_url}">{jenkins_url}</a></li>
    </ul>
  </body>
</html>
"""

part = MIMEText(html, "html")
message.attach(part)

try:
    with smtplib.SMTP("smtp.gmail.com", 587) as server:
        server.starttls()
        server.login(sender_email, password)
        server.sendmail(sender_email, receiver_email, message.as_string())
    print("✅ Mail envoyé !")
except Exception as e:
    print(f"❌ Erreur envoi mail : {e}")
