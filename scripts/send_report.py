import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import xml.etree.ElementTree as ET
import glob
import sys

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

# Récupérer les arguments : build_number, status, jenkins_url
if len(sys.argv) < 4:
    print("Usage: python send_report.py <build_number> <status> <jenkins_url>")
    sys.exit(1)

build_number = sys.argv[1]
status = sys.argv[2]
jenkins_url = sys.argv[3]

total, errors, failures, skipped = parse_test_results()

test_summary = f"{total} tests, {errors} erreurs, {failures} échecs, {skipped} ignorés"

msg = MIMEMultipart('alternative')
msg['Subject'] = f"✅ Rapport pipeline build #{build_number} - {status}"
msg['From'] = 'braininformatiqueservice@gmail.com'
msg['To'] = 'malloukiyoussef22@gmail.com'

body = f"""
<html>
  <body style="font-family: Arial; color: #333;">
    <h2 style="color: #007bff;">📊 Résumé des tests Maven</h2>
    <p><b>Build :</b> #{build_number} &nbsp;&nbsp;&nbsp; <b>Statut :</b> {status}</p>
    <p><b>Résumé :</b> {test_summary}</p>
    <table border="1" cellpadding="5" cellspacing="0" style="border-collapse: collapse;">
      <tr><th>Total</th><th>Erreurs</th><th>Échecs</th><th>Ignorés</th></tr>
      <tr><td>{total}</td><td>{errors}</td><td>{failures}</td><td>{skipped}</td></tr>
    </table>
    <p>
      🚀 <a href="https://hub.docker.com/r/mallouki22/groupe12alinfo-app">Voir l'image Docker</a><br>
      📊 <a href="http://172.19.175.10:9000/projects">Voir le rapport SonarQube</a><br>
      🔗 <a href="{jenkins_url}">Lien vers Jenkins</a>
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
