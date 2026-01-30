# My Blog - Spring Boot API

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

API REST construite avec Spring Boot, MySQL et Docker.

## Prérequis

- [Docker](https://www.docker.com/) et Docker Compose installés sur votre machine.

## Démarrer les conteneurs

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

## Vérifier que tout fonctionne

```bash
docker compose ps
```

Les deux services `db` et `app` doivent afficher l'état **Up**.

L'API est accessible sur : http://localhost:8080/api

Exemples de endpoints :

- `GET /api/articles`
- `GET /api/authors`
- `GET /api/categories`

## Arrêter les conteneurs

```bash
docker compose down
```

Pour supprimer également les données de la base :

```bash
docker compose down -v
```
