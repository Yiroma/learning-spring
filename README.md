# My Blog - Spring Boot API

API REST construite avec Spring Boot, MySQL et Docker.

## Prerequis

- [Docker](https://www.docker.com/) et Docker Compose installes sur votre machine.

## Demarrer les conteneurs

1. Cloner le depot et se placer dans le dossier du projet :

```bash
git clone <url-du-depot>
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

L'application demarre automatiquement apres que MySQL soit pret.

## Verifier que tout fonctionne

```bash
docker compose ps
```

Les deux services `db` et `app` doivent afficher l'etat **Up**.

L'API est accessible sur : http://localhost:8080/api

Exemples de endpoints :

- `GET /api/articles`
- `GET /api/authors`
- `GET /api/categories`

## Arreter les conteneurs

```bash
docker compose down
```

Pour supprimer egalement les donnees de la base :

```bash
docker compose down -v
```
