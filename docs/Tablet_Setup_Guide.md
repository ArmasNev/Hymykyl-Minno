# HyMy-kylä App — Tablet Setup Guide / Tabletin Asennusohje

Tämä asiakirja sisältää vaiheittaiset asennusohjeet **suomeksi** ja **englanniksi** tablettien konfigurointiin HyMy-kylä -palautesovellusta varten.

---

## 🇫🇮 Suomi: Tabletin Asennusohje

### Vaihe 1: Asenna sovellus tabletille
1. Siirrä `hymykyla.apk` -tiedosto tabletille (USB-kaapelilla, muistikortilla tai sähköpostitse).
2. Avaa tabletin **Omat tiedostot** (My Files) -sovellus -> Mene osioon **Lataukset** tai **Asennustiedostot**.
3. Napauta `hymykyla.apk` ja valitse **Asenna** *(salli tarvittaessa asennus tuntemattomista lähteistä)*.

### Vaihe 2: Aseta HyMy-kylä oletuskotisovellukseksi (Automaattinen kokoruutukäynnistys)
1. Avaa tabletin **Asetukset** -> Valitse **Sovellukset**.
2. Valitse **Valitse oletussovellukset** -> Valitse **Kotisovellus**.
3. Valitse listalta **HyMy-kylä**.
4. **Tulos**: Aina kun tabletti käynnistetään uudelleen, se avautuu automaattisesti suoraan **HyMy-kylä** -kyselysovellukseen kokoruututilassa.

### Vaihe 3: Automaattinen Kiosk-tila (Kooditasoinen lukitus)
* **Sovelluksen oma koodilukitus**: HyMy-kylä -sovellus piilottaa automaattisesti Androidin navigaatio- ja tilapalkit (Koti-, Takaisin- ja Viimeisimmät-painikkeet) aina kun sovellus on päällä.
* **Tulos**: Asiakkaat eivät näe navigaatiopainikkeita eivätkä pääse poistumaan sovelluksesta.

### Vaihe 4: Laitetason lisälukitus (Sovelluksen kiinnitys)
1. Avaa tabletin **Asetukset** -> **Turvallisuus ja tietosuoja** -> **Muut turvallisuusasetukset**.
2. Kytke **Sovelluksen kiinnitys** päälle (**ON**) ja kytke päälle *"Kysy suojakoodia ennen kiinnityksen poistamista"*.

### Vaihe 5: Henkilökunnan pääsy ylläpitoon ja CSV-raportin siirto
1. **Avaa ylläpito**: Avaa **HyMy-kylä** -> Paina **Metropolia-logoa pohjassa 3,5 sekuntia**.
2. **Tunnistautuminen**: Syötä ylläpidon salasana -> Syötä tabletin PIN-koodi (mikäli laite kiinnitetty) päästäksesi ylläpitopaneeliin.
3. **Generoi CSV-raportti**: Napauta ylläpidossa **"Tallenna laitteen Lataukset-kansioon"**.
4. **Raportin siirtäminen (3 helppoa tapaa)**:
   - **Tapa 1: USB-kaapelilla tietokoneeseen (Ei vaadi verkkoa)**:
     - Kytke USB-kaapeli tabletin USB-C-portista tietokoneeseen.
     - Pyyhkäise tabletin tilapalkki alas -> Napauta USB-ilmoitusta *"Ladataan laitetta USB:n kautta"* -> Valitse **Tiedostonsiirto (MTP)**.
     - Avaa tietokoneella **Tämä tietokone -> Tabletti -> Sisäinen tallennustila -> Downloads** ja kopioi CSV-tiedosto.
   - **Tapa 2: USB-C-muistitikulle tabletin asetusten kautta (Ei vaadi verkkoa)**:
     - Pyyhkäise tilapalkki alas -> Avaa tabletin **Asetukset** -> **Tallennustila** -> **Tiedostot / Muut** -> **Lataukset**.
     - Kytke USB-C-muistitikku tabletin USB-C-porttiin -> Valitse CSV-tiedosto -> Napauta **Kopioi** -> Valitse kohteeksi **USB-muistitikku**.
   - **Tapa 3: Lähetä suoraan sähköpostitse sovelluksesta (Vaatii verkkyyhteyden)**:
     - Napauta ylläpitopaneelissa **Jaa-painiketta** (Share-ikoni tallennuspainikkeen vieressä).
     - Valitse avautuvasta valikosta sähköposti (esim. Outlook tai Gmail) ja lähetä CSV-raportti suoraan sähköpostiin.

---

## 🇬🇧 English: Tablet Setup Guide

### Step 1: Install the Application
1. Copy the `hymykyla.apk` file to the tablet (via USB cable or Google Drive / Downloads).
2. Open the tablet's **My Files** app -> Go to **Downloads** / **Installation Files**.
3. Tap `hymykyla.apk` -> Select **Install** *(if prompted, allow "Install from unknown sources")*.

### Step 2: Set HyMy-kylä as Default Home App (Automatic Fullscreen Boot)
1. Open tablet **Settings** -> Tap **Apps** -> Tap **Choose default apps**.
2. Tap **Home app**.
3. Select **HyMy-kylä** from the list.
4. **Result**: Whenever the tablet powers on or restarts, it automatically launches **HyMy-kylä** in 100% full screen.

### Step 3: Automatic Code-Level Kiosk Mode
* **Built-in Software Lock**: The HyMy-kylä application automatically hides Android System Navigation and Status Bars (Home, Back, Recent Apps) whenever the app is active.
* **Result**: Clients cannot see navigation buttons or leave the app.

### Step 4: Hardware App Pinning Backup
For additional hardware-level locking:
1. Open tablet **Settings** -> **Security & Privacy** -> **Other security settings**.
2. Turn **ON** **App Pinning** and enable *"Ask for PIN before unpinning"*.

### Step 5: Staff Access to Admin Panel & Exporting CSV Reports
1. **Access Admin Panel**: Open **HyMy-kylä** -> Press and hold the **Metropolia logo for 3.5 seconds**.
2. **Authentication**: Enter staff password -> Enter tablet PIN code (if app is pinned) to access Admin Dashboard.
3. **Generate CSV Report**: Tap **"Tallenna laitteen Lataukset-kansioon"**.
4. **Transferring Report (3 Easy Methods)**:
   - **Method 1: Via USB Cable to Laptop (No Internet Required)**:
     - Connect USB cable from tablet's USB-C port to laptop.
     - Swipe down tablet status bar -> Tap the USB notification *"Charging via USB"* -> Select **File Transfer (MTP)**.
     - On laptop File Explorer, open **This PC -> Tablet -> Internal Storage -> Downloads** to view or copy the CSV report.
   - **Method 2: Via Tablet Settings to USB-C Flash Drive (No Internet Required)**:
     - Swipe down status bar -> Open tablet **Settings** -> **Storage** -> **Files / Other** -> **Downloads**.
     - Plug USB-C flash drive (or USB-A drive via USB-C adapter) into tablet -> Select CSV file -> Tap **Copy** -> Select **USB Storage**.
   - **Method 3: Direct Email/Share from Inside the App (Requires Internet)**:
     - In the Admin Dashboard, tap the **Share icon** next to the save button.
     - Choose **Outlook / Gmail / Email** from the Android popup menu to email the CSV report directly.
