# Projeto de Aplicação de Receitas Culinárias

Este projeto é uma aplicação Android para explorar receitas culinárias, desenvolvido como parte da disciplina de Desenvolvimento Mobile.

## Tema Escolhido e Funcionalidades

A aplicação permite aos utilizadores navegar por um vasto catálogo de receitas de todo o mundo. As principais funcionalidades são:
- **Navegação por Categorias:** Ecrã inicial com uma grelha de categorias de refeições.
- **Lista de Receitas:** Visualização de receitas filtradas por categoria.
- **Detalhes da Receita:** Ecrã com informação detalhada, incluindo ingredientes, modo de preparação e imagem.
- **Pesquisa:** Funcionalidade para procurar receitas por nome.
- **Receita Aleatória:** Um botão para descobrir uma receita surpresa.
- **Favoritos:** Possibilidade de guardar receitas para acesso rápido (em desenvolvimento).

## API Utilizada

A aplicação consome a API pública **TheMealDB**.

Principais endpoints utilizados:
- **Categorias:** `https://www.themealdb.com/api/json/v1/1/categories.php`
- **Filtrar por Categoria:** `https://www.themealdb.com/api/json/v1/1/filter.php?c={strCategory}`
- **Detalhes da Receita:** `https://www.themealdb.com/api/json/v1/1/lookup.php?i={idMeal}`
- **Receita Aleatória:** `https://www.themealdb.com/api/json/v1/1/random.php`

## Instruções de Execução

- **Versão Mínima do SDK:** 24
- **Versão Alvo do SDK:** 36

Para executar o projeto, clone o repositório e abra-o no Android Studio. As dependências necessárias serão descarregadas automaticamente via Gradle.

**Principais Dependências:**
- **Retrofit:** Para realizar as requisições HTTP à API.
- **Gson:** Para fazer o parsing dos dados JSON.
- **Coil:** Para carregamento e cache de imagens.
- **Android Navigation Component:** Para gerir a navegação entre os Fragments.

## Desafios e Soluções

Um dos principais desafios foi a gestão do estado da UI de forma assíncrona, especialmente durante o carregamento dos dados da API. A solução foi utilizar `Coroutines` com `LifecycleScope` para garantir que as requisições fossem canceladas quando o Fragment é destruído, evitando memory leaks.

Outro desafio foi a apresentação da lista de ingredientes e medidas, que na API vêm em campos separados (`strIngredient1`, `strMeasure1`, etc.). A solução foi processar estes dados no `ViewModel` ou no `Fragment` para criar uma lista única de ingredientes antes de a apresentar na UI.

## Grupo

- **Daniel Cocharro:**
- **a27823@ipt.pt:** [Seu Email]
- **Tatiana Mendes:**
- **a27648@ipt.pt:** [Email do Colega]
