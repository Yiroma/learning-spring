# My Blog - Spring Boot API

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## Pourquoi ce repository ?

Ce dépôt est un **projet d'apprentissage** réalisé dans le cadre de la formation [Wild Code School](https://www.wildcodeschool.com/). Il retrace ma progression dans la découverte et la prise en main de **Spring Boot** au travers d'une série de quêtes pédagogiques.

Le fil conducteur est la construction d'une **API REST de blog** ("My Blog") qui s'enrichit à chaque quête : d'un simple projet initialisé avec Spring Initializr jusqu'à une application complète, sécurisée, testée et conteneurisée avec Docker.

### Intérêt du dépôt

- **Traçabilité de l'apprentissage** : chaque quête a été développée dans une branche dédiée puis mergée dans `main`, ce qui permet de suivre la progression commit par commit.
- **Référence personnelle** : ce projet sert de base de connaissances pour retrouver rapidement comment implémenter un concept Spring Boot (relations JPA, sécurité JWT, tests unitaires, Docker, etc.).
- **Exemple concret** : il peut servir de point de départ ou de référence pour toute personne qui débute avec Spring Boot et souhaite voir comment les concepts s'assemblent dans un projet réel.

---

## Parcours d'apprentissage

Le projet a été construit de manière **incrémentale** au travers de 18 quêtes. Chaque quête correspond à une branche Git et couvre un concept clé de l'écosystème Spring Boot.

### Fondations

| # | Quête | Concepts abordés |
|---|-------|-----------------|
| 01 | Initialisation d'un projet avec Spring Boot | Spring Initializr, structure Maven, premier lancement |
| 02 | Connexion à la base de données et création d'une entité | Spring Data JPA, configuration datasource, entité `Article` |
| 03 | Mise en place d'un CRUD | `@RestController`, méthodes BREAD (Browse, Read, Edit, Add, Delete) |
| 04 | Requêtes personnalisées avec Spring Data JPA | Query methods, `findByTitle`, `findByContentContaining`, `findTop5By...` |

### Relations et modélisation

| # | Quête | Concepts abordés |
|---|-------|-----------------|
| 05 | Relation Many-to-One unidirectionnelle | `@ManyToOne`, entité `Category`, gestion de la clé étrangère |
| 06 | Bidirectionnalité et DTO | `@OneToMany`, pattern DTO, séparation modèle / représentation API |
| 07 | Relations Many-to-Many sans attribut | `@ManyToMany`, table de jointure automatique, entité `Image` |
| 08 | Relations Many-to-Many avec attributs | Entité d'association `ArticleAuthor`, champ `contribution`, entité `Author` |

### Architecture et robustesse

| # | Quête | Concepts abordés |
|---|-------|-----------------|
| 09 | Les Services | Couche service, pattern Mapper, séparation contrôleur / logique métier |
| 10 | Gestion des erreurs | `@ControllerAdvice`, exceptions personnalisées (`ResourceNotFoundException`, `InvalidRelationException`) |
| 11 | Contraintes de validation | `spring-boot-starter-validation`, annotations Jakarta (`@NotBlank`, `@Size`...), `MethodArgumentNotValidException` |

### Sécurité

| # | Quête | Concepts abordés |
|---|-------|-----------------|
| 12 | Inscription d'utilisateurs | `spring-boot-starter-security`, entité `User`, BCrypt, `UserService` |
| 13 | Authentification d'utilisateurs | JWT (jjwt 0.13.0), `JwtService`, `JwtAuthenticationFilter`, `AuthenticationService` |
| 14 | Gestion des autorisations | Rôles (`ROLE_USER`, `ROLE_ADMIN`), `@PreAuthorize`, contrôle d'accès par endpoint |
| 15 | Gérer les CORS dans une API | Configuration globale CORS, origine paramétrable via variable d'environnement |

### Tests et déploiement

| # | Quête | Concepts abordés |
|---|-------|-----------------|
| 16 | Les tests unitaires | JUnit 5, Mockito, `@MockitoBean`, tests de repositories / services / contrôleurs, H2 en mémoire |
| 17 | Hello Docker | Introduction à Docker, `Dockerfile` multi-stage (Maven build + JRE Alpine) |
| 18 | Dockeriser une application avec Docker Compose | `docker-compose.yml`, orchestration MySQL + Spring Boot, healthcheck, `.env` |

### A venir

| # | Quête | Statut |
|---|-------|--------|
| 19 | Mise en place d'une CI/CD avec GitHub Actions | Non réalisée |

---

## Architecture du projet

```
myblog/
├── src/main/java/org/wildcodeschool/myblog/
│   ├── controller/       # Endpoints REST (Article, Author, Category, Image, Auth, User, Admin)
│   ├── service/          # Logique métier
│   ├── repository/       # Accès aux données (Spring Data JPA)
│   ├── model/            # Entités JPA (Article, Author, Category, Image, ArticleAuthor, User)
│   ├── dto/              # Objets de transfert de données
│   ├── mapper/           # Conversion entité <-> DTO
│   ├── security/         # Configuration Spring Security, JWT, filtres
│   └── exception/        # Exceptions personnalisées et handler global
├── src/test/             # Tests unitaires et d'intégration
├── Dockerfile            # Build multi-stage
├── docker-compose.yml    # Orchestration des conteneurs
└── pom.xml               # Dépendances Maven
```

---

## Lancer le projet

### Prérequis

- [Docker](https://www.docker.com/) et Docker Compose installés sur votre machine.

### Démarrer les conteneurs

1. Cloner le dépôt et se placer dans le dossier du projet :

```bash
git clone https://github.com/Yiroma/learning-spring
cd myblog
```

2. Copier le fichier `.env.sample` en `.env` et renseigner vos propres valeurs :

```bash
cp .env.sample .env
```

3. Lancer les conteneurs :

```bash
docker compose up --build
```

L'application démarre automatiquement après que MySQL soit prêt.

### Vérifier que tout fonctionne

```bash
docker compose ps
```

Les deux services `db` et `app` doivent afficher l'état **Up**.

L'API est accessible sur : http://localhost:8080/api

Exemples de endpoints :

- `GET /api/articles`
- `GET /api/authors`
- `GET /api/categories`

### Arrêter les conteneurs

```bash
docker compose down
```

Pour supprimer également les données de la base :

```bash
docker compose down -v
```

---

## Explorer l'historique

Chaque quête ayant été développée dans sa propre branche, il est possible de naviguer dans l'historique Git pour revoir l'évolution du projet :

```bash
# Voir toutes les branches
git branch -a

# Voir l'historique complet avec le graphe des branches
git log --oneline --all --graph --decorate

# Se placer sur une branche spécifique pour voir le code à cette étape
git checkout 03-implementation-of-a-crud
```
