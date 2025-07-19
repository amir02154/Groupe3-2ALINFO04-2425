import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import xml.etree.ElementTree as ET
import glob

def parse_test_results():
    total, errors, failures, skipped = 0, 0, 0, 0
    for file in glob.glob('target/surefire-reports/*.xml'):
        tree = ET.parse(file)
        root = tree.getroot()
        total += int(root.attrib.get('tests', 0))
        errors += int(root.attrib.get('errors', 0))
        failures += int(root.attrib.get('failures', 0))
        skipped += int(root.attrib.get('skipped', 0))
    return total, errors, failures, skipped

total, errors, failures, skipped = parse_test_results()

msg = MIMEMultipart('alternative')
msg['Subject'] = "✅ Rapport de tests - Projet Spring Boot"
msg['From'] = 'braininformatiqueservice@gmail.com'
msg['To'] = 'malloukiyoussef22@gmail.com'

body = f"""
<html>
  <body style="font-family: Arial; color: #333;">
    <h2 style="color: #007bff;">📊 Résumé des tests Maven</h2>
    <table border="1" cellpadding="5" cellspacing="0" style="border-collapse: collapse;">
      <tr><th>Total</th><th>Erreurs</th><th>Échecs</th><th>Ignorés</th></tr>
      <tr><td>{total}</td><td>{errors}</td><td>{failures}</td><td>{skipped}</td></tr>
    </table>
    <p>
      🚀 <a href="https://hub.docker.com/repository/docker/mallouki22/groupe12alinfo-app/tags/1.4.1/sha256-e578d1a7a294141a488f1f0295e04f51a94e6d7618226f0d6a4873306d6c3433?tab=layers">Voir l'image Docker</a><br>
      📊 <a href="http://172.19.175.10:9000/projects">Voir le rapport SonarQube</a>
    </p>
  </body>
</html>
"""

msg.attach(MIMEText(body, 'html'))

with smtplib.SMTP('smtp.gmail.com', 587) as server:
    server.starttls()
    server.login('braininformatiqueservice@gmail.com', 'kcefeysilehngbxw')
    server.sendmail(msg['From'], msg['To'], msg.as_string())

print("✅ Mail envoyé avec succès !")
