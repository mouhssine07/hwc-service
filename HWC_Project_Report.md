# Rapport Complet Projet HWC - Harmony Works Consulting
## Pour Claude Code CLI - Continuation du projet

---

## 1. CONTEXTE DU PROJET

**Nom du projet :** Harmony Works Consulting (HWC)
**Type :** Site web vitrine avec back-office
**Backend :** Spring Boot + MySQL
**Package :** hwc_backend
**Objectif :** Créer un backend complet pour le site HWC

HWC est une société de consulting présente dans 4 pays :
- Maroc (Casablanca)
- France (Paris)
- Luxembourg
- Iles Maurice

### 3 domaines d'expertise :
1. Marketing Digital
2. Leadership & Performance Humaine
3. Performance sous pression (Krav Maga)

---

## 2. CONFIGURATION SPRING BOOT

### application.properties
```properties
spring.application.name=hwc-backend


spring.datasource.url=jdbc:mysql://localhost:3306/hwc_db?createDatabaseIfNotExist=true
spring.datasource.username=Harmony
spring.datasource.password=H@rmony
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true


server.port=8080
```

### Dépendances Maven
- Spring Web
- Spring Data JPA
- MySQL Driver
- Lombok
- Spring Boot DevTools

### Structure du projet
```
hwc-backend
├── src/main/java/hwc_backend
│   ├── entity/
│   ├── repository/
│   ├── dto/
│   ├── service/
│   └── controller/
└── src/main/resources/
    └── application.properties
```

---

## 3. RÈGLES APPLIQUÉES POUR LA BDD

1. **Tout contenu dynamique = table en BDD**
2. **Contenu statique (titres fixes, boutons) = pas de table**
3. **Le champ `ordre` est géré par le frontend = supprimé de toutes les tables**
4. **Tables de jointure = pas de DTO séparé**

---

## 4. TOUTES LES TABLES CRÉÉES

### 4.1 Tables Indépendantes (sans FK)

#### clients_confiance
```sql
CREATE TABLE clients_confiance (
    id       INT PRIMARY KEY AUTO_INCREMENT,
    nom      VARCHAR(100),
    logo_url VARCHAR(255)
);
```
Contexte : Logos "Ils nous font confiance" - Hero page accueil (JCC, Interflon)

---

#### chiffres_cles
```sql
CREATE TABLE chiffres_cles (
    id      INT PRIMARY KEY AUTO_INCREMENT,
    valeur  VARCHAR(20),
    libelle VARCHAR(100)
);
```
Contexte : Section "Qui sommes-nous" (+20 ans, 98%, 4 pays)

---

#### services
```sql
CREATE TABLE services (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    accroche    VARCHAR(100),
    titre       VARCHAR(100),
    description TEXT,
    icone       VARCHAR(50)
);
```
Contexte : 3 domaines principaux - Table centrale/parent du projet

---

#### etiquettes
```sql
CREATE TABLE etiquettes (
    id  INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50)
);
```
Contexte : Tags sur chaque service (Lead generation, SEO, Coaching...)

---

#### certifications
```sql
CREATE TABLE certifications (
    id       INT PRIMARY KEY AUTO_INCREMENT,
    nom      VARCHAR(100),
    logo_url VARCHAR(255),
    type     VARCHAR(50)
);
```
Contexte : Section "Certifications & Partenaires"
Note : type = "HWC" ou "STEPHANE"

---

#### temoignages
```sql
CREATE TABLE temoignages (
    id       INT PRIMARY KEY AUTO_INCREMENT,
    nom      VARCHAR(100),
    logo_url VARCHAR(255),
    type     VARCHAR(50)
);
```
Contexte : Section "Avis" clients

---

#### pays
```sql
CREATE TABLE pays (
    id        INT PRIMARY KEY AUTO_INCREMENT,
    nom       VARCHAR(100),
    code_pays VARCHAR(5),
    ville     VARCHAR(100)
);
```
Contexte : Section "Présence internationale"
Note : latitude/longitude supprimées

---

#### demandes_contact
```sql
CREATE TABLE demandes_contact (
    id    INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(150)
);
```
Contexte : Formulaire de contact - seulement email stocké

---

### 4.2 Tables Avec Relations

#### service_etiquettes (ManyToMany)
```sql
CREATE TABLE service_etiquettes (
    service_id   INT,
    etiquette_id INT,
    PRIMARY KEY (service_id, etiquette_id),
    FOREIGN KEY (service_id)   REFERENCES services(id),
    FOREIGN KEY (etiquette_id) REFERENCES etiquettes(id)
);
```
Relation : services ManyToMany etiquettes

---

#### sous_services
```sql
CREATE TABLE sous_services (
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    titre                VARCHAR(100),
    accroche             VARCHAR(255),
    description          TEXT,
    description_complete TEXT,
    icone                VARCHAR(50),
    service_id           INT,
    FOREIGN KEY (service_id) REFERENCES services(id)
);
```
Relation : services (1) → sous_services (N)
Note : description_complete = contenu affiché après "Voir plus"

---

#### service_fonctionnalites
```sql
CREATE TABLE service_fonctionnalites (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    contenu    VARCHAR(255),
    service_id INT,
    FOREIGN KEY (service_id) REFERENCES services(id)
);
```
Relation : services (1) → service_fonctionnalites (N)

---

#### service_images
```sql
CREATE TABLE service_images (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    image_url  VARCHAR(255),
    service_id INT,
    FOREIGN KEY (service_id) REFERENCES services(id)
);
```
Relation : services (1) → service_images (N)

---

#### sous_service_fonctionnalites
```sql
CREATE TABLE sous_service_fonctionnalites (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    contenu         VARCHAR(255),
    sous_service_id INT,
    FOREIGN KEY (sous_service_id) REFERENCES sous_services(id)
);
```
Relation : sous_services (1) → sous_service_fonctionnalites (N)

---

#### sous_service_avantages
```sql
CREATE TABLE sous_service_avantages (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    titre           VARCHAR(255),
    description     TEXT,
    sous_service_id INT,
    FOREIGN KEY (sous_service_id) REFERENCES sous_services(id)
);
```
Relation : sous_services (1) → sous_service_avantages (N)

---

#### sous_service_etapes
```sql
CREATE TABLE sous_service_etapes (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    numero          INT,
    titre           VARCHAR(100),
    description     TEXT,
    sous_service_id INT,
    FOREIGN KEY (sous_service_id) REFERENCES sous_services(id)
);
```
Relation : sous_services (1) → sous_service_etapes (N)

---

#### sous_service_faqs
```sql
CREATE TABLE sous_service_faqs (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    question        VARCHAR(255),
    reponse         TEXT,
    sous_service_id INT,
    FOREIGN KEY (sous_service_id) REFERENCES sous_services(id)
);
```
Relation : sous_services (1) → sous_service_faqs (N)

---

#### accompagnements
```sql
CREATE TABLE accompagnements (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    titre           VARCHAR(100),
    accroche        VARCHAR(255),
    description_1   TEXT,
    description_2   TEXT,
    sous_service_id INT,
    FOREIGN KEY (sous_service_id) REFERENCES sous_services(id)
);
```
Contexte : Page Leadership Section 4 - 5 accordéons
Relation : sous_services (1) → accompagnements (N)

---

## 5. SCHÉMA DES RELATIONS COMPLET

```
clients_confiance     → indépendante
chiffres_cles         → indépendante
certifications        → indépendante
temoignages           → indépendante
pays                  → indépendante
demandes_contact      → indépendante

services (PARENT CENTRAL)
    ├── service_etiquettes (ManyToMany avec etiquettes)
    ├── sous_services
    │   ├── sous_service_fonctionnalites
    │   ├── sous_service_avantages
    │   ├── sous_service_etapes
    │   ├── sous_service_faqs
    │   └── accompagnements
    ├── service_fonctionnalites
    └── service_images

etiquettes (PARENT)
    └── service_etiquettes (ManyToMany avec services)
```

---

## 6. ENTITIES SPRING BOOT CRÉÉES

### ClientsConfiance.java
```java
@Entity
@Table(name = "clients_confiance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientsConfiance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String logoUrl;
}
```

### ChiffresCles.java
```java
@Entity
@Table(name = "chiffres_cles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiffresCles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String valeur;
    private String libelle;
}
```

### Services.java
```java
@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accroche;
    private String titre;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String icone;
}
```

### Etiquettes.java
```java
@Entity
@Table(name = "etiquettes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etiquettes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
}
```

### Certifications.java
```java
@Entity
@Table(name = "certifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String logoUrl;
    private String type;
}
```

### Temoignages.java
```java
@Entity
@Table(name = "temoignages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Temoignages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String logoUrl;
    private String type;
}
```

### Pays.java
```java
@Entity
@Table(name = "pays")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String codePays;
    private String ville;
}
```

### SousServices.java
```java
@Entity
@Table(name = "sous_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousServices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String accroche;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String descriptionComplete;
    private String icone;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services service;
}
```

### ServiceEtiquettes.java
```java
@Entity
@Table(name = "service_etiquettes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEtiquettes {
    @EmbeddedId
    private ServiceEtiquettesId id;
    @ManyToOne
    @MapsId("serviceId")
    @JoinColumn(name = "service_id")
    private Services service;
    @ManyToOne
    @MapsId("etiquetteId")
    @JoinColumn(name = "etiquette_id")
    private Etiquettes etiquette;
}
```

### ServiceEtiquettesId.java
```java
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEtiquettesId implements Serializable {
    private Long serviceId;
    private Long etiquetteId;
}
```

### ServiceFonctionnalites.java
```java
@Entity
@Table(name = "service_fonctionnalites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceFonctionnalites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contenu;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services service;
}
```

### ServiceImages.java
```java
@Entity
@Table(name = "service_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services service;
}
```

### SousServiceFonctionnalites.java
```java
@Entity
@Table(name = "sous_service_fonctionnalites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousServiceFonctionnalites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contenu;
    @ManyToOne
    @JoinColumn(name = "sous_service_id")
    private SousServices sousService;
}
```

### SousServiceAvantages.java
```java
@Entity
@Table(name = "sous_service_avantages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousServiceAvantages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    @JoinColumn(name = "sous_service_id")
    private SousServices sousService;
}
```

### SousServiceEtapes.java
```java
@Entity
@Table(name = "sous_service_etapes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousServiceEtapes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numero;
    private String titre;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    @JoinColumn(name = "sous_service_id")
    private SousServices sousService;
}
```

### SousServiceFaqs.java
```java
@Entity
@Table(name = "sous_service_faqs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousServiceFaqs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    @Column(columnDefinition = "TEXT")
    private String reponse;
    @ManyToOne
    @JoinColumn(name = "sous_service_id")
    private SousServices sousService;
}
```

### Accompagnements.java
```java
@Entity
@Table(name = "accompagnements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Accompagnements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String accroche;
    @Column(columnDefinition = "TEXT")
    private String description1;
    @Column(columnDefinition = "TEXT")
    private String description2;
    @ManyToOne
    @JoinColumn(name = "sous_service_id")
    private SousServices sousService;
}
```

### DemandesContact.java
```java
@Entity
@Table(name = "demandes_contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandesContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
}
```

---

## 7. REPOSITORIES CRÉÉS

Tous les repositories étendent JpaRepository :

```java
// Exemple pattern pour tous les repositories
@Repository
public interface XxxRepository extends JpaRepository<Xxx, Long> {}
```

Liste complète :
- ClientsConfianceRepository → JpaRepository<ClientsConfiance, Long>
- ChiffresClesRepository → JpaRepository<ChiffresCles, Long>
- ServicesRepository → JpaRepository<Services, Long>
- EtiquettesRepository → JpaRepository<Etiquettes, Long>
- CertificationsRepository → JpaRepository<Certifications, Long>
- TemoignagesRepository → JpaRepository<Temoignages, Long>
- PaysRepository → JpaRepository<Pays, Long>
- SousServicesRepository → JpaRepository<SousServices, Long>
- ServiceEtiquettesRepository → JpaRepository<ServiceEtiquettes, ServiceEtiquettesId>
- ServiceFonctionnalitesRepository → JpaRepository<ServiceFonctionnalites, Long>
- ServiceImagesRepository → JpaRepository<ServiceImages, Long>
- SousServiceFonctionnalitesRepository → JpaRepository<SousServiceFonctionnalites, Long>
- SousServiceAvantagesRepository → JpaRepository<SousServiceAvantages, Long>
- SousServiceEtapesRepository → JpaRepository<SousServiceEtapes, Long>
- SousServiceFaqsRepository → JpaRepository<SousServiceFaqs, Long>
- AccompagnementsRepository → JpaRepository<Accompagnements, Long>
- DemandesContactRepository → JpaRepository<DemandesContact, Long>

---

## 8. DTOs CRÉÉS

### ClientsConfianceDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class ClientsConfianceDTO {
    private Long id;
    private String nom;
    private String logoUrl;
}
```

### ChiffresClesDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class ChiffresClesDTO {
    private Long id;
    private String valeur;
    private String libelle;
}
```

### ServicesDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class ServicesDTO {
    private Long id;
    private String accroche;
    private String titre;
    private String description;
    private String icone;
    private List<EtiquettesDTO> etiquettes; // inclus directement - pas de ServiceEtiquettesDTO
}
```

### EtiquettesDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class EtiquettesDTO {
    private Long id;
    private String nom;
}
```

### CertificationsDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class CertificationsDTO {
    private Long id;
    private String nom;
    private String logoUrl;
    private String type;
}
```

### TemoignagesDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class TemoignagesDTO {
    private Long id;
    private String nom;
    private String logoUrl;
    private String type;
}
```

### PaysDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class PaysDTO {
    private Long id;
    private String nom;
    private String codePays;
    private String ville;
}
```

### SousServicesDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class SousServicesDTO {
    private Long id;
    private String titre;
    private String accroche;
    private String description;
    private String descriptionComplete;
    private String icone;
    private Long serviceId;
    private String serviceTitre;
}
```

### ServiceFonctionnalitesDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class ServiceFonctionnalitesDTO {
    private Long id;
    private String contenu;
    private Long serviceId;
}
```

### ServiceImagesDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class ServiceImagesDTO {
    private Long id;
    private String imageUrl;
    private Long serviceId;
}
```

### SousServiceFonctionnalitesDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class SousServiceFonctionnalitesDTO {
    private Long id;
    private String contenu;
    private Long sousServiceId;
}
```

### SousServiceAvantagesDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class SousServiceAvantagesDTO {
    private Long id;
    private String titre;
    private String description;
    private Long sousServiceId;
}
```

### SousServiceEtapesDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class SousServiceEtapesDTO {
    private Long id;
    private Integer numero;
    private String titre;
    private String description;
    private Long sousServiceId;
}
```

### SousServiceFaqsDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class SousServiceFaqsDTO {
    private Long id;
    private String question;
    private String reponse;
    private Long sousServiceId;
}
```

### AccompagnementsDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class AccompagnementsDTO {
    private Long id;
    private String titre;
    private String accroche;
    private String description1;
    private String description2;
    private Long sousServiceId;
}
```

### DemandesContactDTO
```java
@Data @NoArgsConstructor @AllArgsConstructor
public class DemandesContactDTO {
    private Long id;
    private String email;
}
```

---

## 9. PROCHAINES ÉTAPES À FAIRE

### Étape suivante : Services Layer
Pour chaque entity créer :

```java
// Interface
public interface ServicesService {
    List<ServicesDTO> getAllServices();
    ServicesDTO getServiceById(Long id);
    ServicesDTO createService(ServicesDTO dto);
    ServicesDTO updateService(Long id, ServicesDTO dto);
    void deleteService(Long id);
}

// Implementation
@Service
public class ServicesServiceImpl implements ServicesService {
    // ...
}
```

### Ensuite : Controllers
```java
@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServicesController {
    // GET, POST, PUT, DELETE endpoints
}
```

---



## 10. RÉSUMÉ COMPTAGE

```
Tables créées    : 17
Entities créées  : 17
Repositories     : 17
DTOs créés       : 16 (pas de DTO pour ServiceEtiquettes)
Services         : À CRÉER
Controllers      : À CRÉER
```
