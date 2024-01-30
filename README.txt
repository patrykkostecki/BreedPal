
########################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################
POL
########################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################

Aplikacja na Androida: ZooZoom

Przegląd

"ZooZoom" to aplikacja na Androida opracowana w języku Java, która oferuje funkcje wysyłania spersonalizowanych wiadomości powitalnych oraz rozpoznawania obrazów za pomocą modelu MobileNetV1. Aplikacja została zaprojektowana z myślą o prostocie i intuicyjności, umożliwiając łatwe korzystanie z zaawansowanych technologii ML.

Funkcje

MainActivity: Umożliwia wprowadzanie przez użytkownika wiadomości, które są przekazywane do następnego ekranu.
DisplayMessageActivity: Wyświetla spersonalizowaną wiadomość powitalną, umożliwiając użytkownikowi wgląd w przesłaną treść.
ModelDisplay: Oferuje możliwość wyboru zdjęcia z galerii lub wykonania nowego zdjęcia, które następnie jest klasyfikowane przy użyciu modelu MobileNetV1.
Instalacja

Aby zainstalować "ZooZoom" na telefonie Android, wykonaj poniższe kroki:

Sklonuj repozytorium na swoje lokalne środowisko.
Otwórz projekt w Android Studio.
Zbuduj projekt, wybierając Build > Build Bundle(s) / APK(s) > Build APK(s).
Po zbudowaniu, znajdź plik APK w folderze build projektu.
Przenieś plik APK na swój telefon Android.
Na telefonie otwórz plik APK i postępuj zgodnie z instrukcjami na ekranie, aby zainstalować aplikację. (Upewnij się, że włączona jest opcja instalacji z nieznanych źródeł).
Wymagania

Android Studio
Min SDK Version:17.0.7
Target SDK Version: 17.0.7
Uprawnienia

Aplikacja wymaga następujących uprawnień:

Dostęp do kamery: Do robienia zdjęć.
Dostęp do pamięci urządzenia: Do wyboru zdjęć z galerii.
Struktura Projektu

Projekt zawiera następujące główne katalogi i pliki:

MainActivity.java: Główny plik aktywności.
DisplayMessageActivity.java: Aktywność do wyświetlania wiadomości.
ModelDisplay.java: Aktywność do przetwarzania i klasyfikacji obrazów.
res/layout: Zawiera pliki XML dla interfejsu użytkownika.
AndroidManifest.xml: Definiuje strukturę i konfigurację aplikacji.
assets: Zawiera zasoby, takie jak modele ML i etykiety.
Biblioteki i Narzędzia

TensorFlow Lite: Biblioteka do wdrażania modeli uczenia maszynowego na urządzeniach mobilnych.
Autorzy

Szymon Czermak
Patryk Kostecki
************************************************************************************************

########################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################
ENG
########################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################################

Android Application: ZooZoom

Overview

"ZooZoom" is a Java-developed Android application that offers features for sending personalized welcome messages and image recognition using the MobileNetV1 model. The app has been designed with simplicity and intuitiveness in mind, allowing easy use of advanced ML technologies.

Features

MainActivity: Allows users to enter messages, which are then passed to the next screen.
DisplayMessageActivity: Displays a personalized welcome message, allowing the user to view the sent content.
ModelDisplay: Offers the ability to choose a photo from the gallery or take a new photo, which is then classified using the MobileNetV1 model.
Installation

To install "ZooZoom" on an Android phone, follow these steps:

Clone the repository to your local environment.
Open the project in Android Studio.
Build the project by selecting Build > Build Bundle(s) / APK(s) > Build APK(s).
After building, find the APK file in the build folder of the project.
Transfer the APK file to your Android phone.
On the phone, open the APK file and follow the on-screen instructions to install the application. (Make sure the option to install from unknown sources is enabled).
Requirements

Android Studio
Min SDK Version:17.0.7
Target SDK Version:17.0.7
Permissions

The application requires the following permissions:

Camera Access: For taking photos.
Device Storage Access: For selecting photos from the gallery.
Project Structure

The project contains the following main directories and files:

MainActivity.java: Main activity file.
DisplayMessageActivity.java: Activity for displaying messages.
ModelDisplay.java: Activity for processing and classifying images.
res/layout: Contains XML files for the user interface.
AndroidManifest.xml: Defines the structure and configuration of the application.
assets: Contains resources such as ML models and labels.
Libraries and Tools

TensorFlow Lite: A library for implementing machine learning models on mobile devices.
Authors

Szymon Czermak
Patryk Kostecki
***********************************************************************************************