# Monitoreo de Alimento en Silos - App Android

Proyecto correspondiente a la asignatura **DSY1105 · Desarrollo de Aplicaciones Móviles** (Duoc UC - Sede Melipilla).

## 👥 Integrantes
* **Alam Cueto** 
* **Francesca Valencia** 
* **Docente:** Fernando Sepúlveda

---

## 📱 Descripción del Proyecto
Aplicación móvil desarrollada para operarios, supervisores y jefaturas de empresas del sector agroalimentario. Permite monitorear el nivel de inventario en silos por granja, registrar cargas y consumos en zonas con conectividad limitada (Offline-First) y gestionar alertas preventivas para evitar el desabastecimiento.

---

## 🛠️ Stack Tecnológico
* **Lenguaje:** Kotlin
* **UI:** Jetpack Compose / Material Design 3
* **Arquitectura:** MVVM (Model-View-ViewModel) + Clean Architecture (por capas)
* **Persistencia Local:** Room Database
* **Conexión API:** Retrofit 2 + OkHttp
* **Tareas en segundo plano / Sync:** WorkManager
* **Mínimo SDK Soportado:** API 24 (Android 7.0)

---

## 🏗️ Estructura del Proyecto (Capas)
* `data/`: Entidades de Room, DAOs, API Services y Repositorios.
* `domain/`: Casos de uso y modelos de negocio.
* `ui/`: ViewModels, Pantallas (Screens) y Componentes de interfaz.
* `util/`: Clases auxiliares, gestores de red y notificaciones.

---

## 🚀 Cómo ejecutar el proyecto
1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/TU_USUARIO/dsy1105-monitoreo-silos.git](https://github.com/TU_USUARIO/dsy1105-monitoreo-silos.git)

## Trello
   ```bash
https://trello.com/invite/b/6ae53a109d016c1436c518d4/ATTI1537ad52ea9c45cffe77c8800cccd4488C090F5F/silos
