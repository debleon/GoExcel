# 📱 GOEXCEL - App Educativa Interactiva

GOEXCEL es una aplicación Android diseñada para ofrecer una experiencia de aprendizaje gamificada e interactiva, enfocada en potenciar el conocimiento de los estudiantes mediante desafíos, módulos temáticos y retroalimentación personalizada. Está integrada con Firebase para la autenticación, almacenamiento y gestión de datos en tiempo real.

---

## 📌 Índice

- [🎯 Objetivo del Proyecto](#-objetivo-del-proyecto)
- [🛠️ Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [🚀 Funcionalidades Principales](#-funcionalidades-principales)
- [🔐 Autenticación y Seguridad](#-autenticación-y-seguridad)
- [⚙️ Cómo Ejecutar el Proyecto](#-cómo-ejecutar-el-proyecto)
- [🧑‍💻 Autores](#-autores)
- [📜 Licencia](#-licencia)

---

## 🎯 Objetivo del Proyecto

Desarrollar una aplicación móvil educativa que motive a los estudiantes a aprender por medio de:

- Módulos de contenido por niveles.
- Desafíos interactivos.
- Sistema de avance y retroalimentación.
- Almacenamiento de progreso

Este proyecto busca brindar una solución digital para fortalecer el proceso educativo, promoviendo el autoaprendizaje y el seguimiento del progreso del usuario.

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje principal:** Kotlin
- **IDE:** Android Studio
- **Base de Datos:** Firebase Realtime Database
- **Autenticación:** Firebase Authentication (email y contraseña)
- **Control de versiones:** Git + GitHub
- **Diseño UI:** XML, Material Design
- **Otros:** Google Services, Recyclerview, WebView, AlertDialog

---

## 🚀 Funcionalidades Principales

- ✅ Registro e inicio de sesión de usuarios.
- ✅ Vista de módulos educativos (por ejemplo: Módulo 1, 2, 3...).
- ✅ Sistema de desafíos interactivos y respuestas 
- ✅ Seguimiento del nivel del usuario desde Firebase.
- ✅ Logout seguro desde el menú lateral.
- ✅ Interfaz intuitiva y responsiva.
- ✅ Acceso en tiempo real a contenido educativo.

---


## 🔐 Autenticación y Seguridad

La autenticación se gestiona con **Firebase Authentication**, utilizando inicio de sesión por correo electrónico y contraseña.  
Cada usuario tiene su propio `UID`, el cual se utiliza para guardar y recuperar su progreso de forma segura en **Firebase Realtime Database**.

Además:

- 🔒 Las reglas de seguridad están configuradas para permitir acceso solo a usuarios autenticados.
- 🔄 Los datos se sincronizan automáticamente con la nube.

---

## ⚙️ Cómo Ejecutar el Proyecto

1. Clona el repositorio:
   ```bash
   git clone https://github.com/debleon/GoExcel

2. Ejecuta en un emulador o dispositivo físico
   
    Nota: El archivo google-services.json ya está incluido en el proyecto, por lo que no se requiere configuración adicional de Firebase.
---

## 🧑‍💻 Autores
Nombres:
   1. Brayan Andres Bellaizan
   4. Deby Johana Leon
---
##📜 Licencia

   Este proyecto se realiza con fines académicos en el marco de la materia Construcci{on de Aplicaciones Moviles
en la Universidad Jorge Tadeo Lozano.
Todos los derechos están reservados por los autores y su uso externo debe contar con aprobación previa.


