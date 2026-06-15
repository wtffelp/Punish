# Punish 🎮🏆

> Sistema de gerenciamento de torneios de jogos de luta — Double Elimination, FT3, e bracket automático.

## Sobre

**Punish** é um sistema backend para criar e gerenciar torneios de fighting games (Tekken, Street Fighter, Guilty Gear, Smash, etc.) com chaveamento **Double Elimination**, registro de resultados e atualização automática do bracket.

Inspirado no [Start.gg](https://start.gg), mas focado em campeonatos locais e torneios de faculdade.

---

## 🏗️ Estrutura do Projeto

```
com.punish
├── Main.java
├── Config/
│   └── Database.java
├── Model/
│   ├── TournamentStatus.java          # enum
│   ├── BracketType.java               # enum
│   ├── MatchStatus.java               # enum
│   ├── Tournament.java
│   ├── Player.java
│   ├── TournamentPlayer.java
│   └── Match.java
├── Repository/
│   ├── TournamentRepository.java
│   ├── PlayerRepository.java
│   ├── TournamentPlayerRepository.java
│   └── MatchRepository.java
├── Service/
│   ├── TournamentService.java
│   ├── PlayerService.java
│   ├── MatchService.java
│   └── BracketService.java
├── Controller/
│   ├── TournamentController.java
│   ├── PlayerController.java
│   └── MatchController.java
└── JWT/                               # futuro (P3)
    ├── JWTService.java
    └── AuthMiddleware.java
```

---

## 📦 Dependências (`pom.xml`)

```xml
<!-- HTTP -->
<dependency>
    <groupId>io.javalin</groupId>
    <artifactId>javalin</artifactId>
    <version>6.7.0</version>
</dependency>

<!-- JSON -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.20.0</version>
</dependency>

<!-- PostgreSQL -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.7</version>
</dependency>

<!-- Pool de conexões -->
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>6.3.0</version>
</dependency>

<!-- JDBI -->
<dependency>
    <groupId>org.jdbi</groupId>
    <artifactId>jdbi3-core</artifactId>
    <version>3.45.0</version>
</dependency>

<!-- Testes -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.13.4</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.27.3</version>
    <scope>test</scope>
</dependency>
```

---

## 🗃️ Model

### TournamentStatus.java (enum)

```java
package com.punish.Model;

public enum TournamentStatus {
    CREATED, STARTED, FINISHED
}
```

### BracketType.java (enum)

```java
package com.punish.Model;

public enum BracketType {
    WINNERS, LOSERS, GRAND_FINAL
}
```

### MatchStatus.java (enum)

```java
package com.punish.Model;

public enum MatchStatus {
    WAITING, READY, IN_PROGRESS, FINISHED
}
```

### Tournament.java

| Campo | Tipo | Mapeamento SQL |
|-------|------|----------------|
| `id` | `long` | `BIGSERIAL PK` |
| `nome` | `String` | `VARCHAR(100)` |
| `game` | `String` | `VARCHAR(50)` |
| `status` | `String` | `VARCHAR(20)` |
| `criado_em` | `Timestamp` | `TIMESTAMP` |

Métodos: construtor vazio, getters, setters.

### Player.java

| Campo | Tipo | Mapeamento SQL |
|-------|------|----------------|
| `id` | `long` | `BIGSERIAL PK` |
| `nickname` | `String` | `VARCHAR(50) UNIQUE` |

Métodos: construtor vazio, getters, setters.

### TournamentPlayer.java

| Campo | Tipo | Mapeamento SQL |
|-------|------|----------------|
| `id` | `long` | `BIGSERIAL PK` |
| `fk_tournament_id` | `long` | `FK → tournament(id)` |
| `fk_player_id` | `long` | `FK → player(id)` |
| `seed` | `Integer` | `INT` |

Métodos: construtor vazio, getters, setters.

### Match.java

| Campo | Tipo | Mapeamento SQL |
|-------|------|----------------|
| `id` | `long` | `BIGSERIAL PK` |
| `fk_tournament_id` | `long` | `FK → tournament(id)` |
| `fk_player1_id` | `Long` | `FK → player(id), nullable` |
| `fk_player2_id` | `Long` | `FK → player(id), nullable` |
| `fk_winner_id` | `Long` | `FK → player(id), nullable` |
| `score_player1` | `Integer` | `INT, nullable` |
| `score_player2` | `Integer` | `INT, nullable` |
| `bracket_type` | `String` | `VARCHAR(20)` |
| `round_number` | `int` | `INT` |
| `match_number` | `Integer` | `INT, nullable` |
| `fk_next_match_win_id` | `Long` | `FK → match(id), nullable` |
| `fk_next_match_lose_id` | `Long` | `FK → match(id), nullable` |
| `status` | `String` | `VARCHAR(20) DEFAULT 'WAITING'` |

Métodos: construtor vazio, getters, setters.

---

## 🗄️ Database

### Config/Database.java

Singleton JDBI. Idêntico ao padrão da Weiva:

```java
package com.punish.Config;

import org.jdbi.v3.core.Jdbi;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {
    private static Jdbi jdbi;

    public static Jdbi getJdbi() {
        if (jdbi == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(System.getenv("DB_URL"));
            config.setUsername(System.getenv("DB_USER"));
            config.setPassword(System.getenv("DB_PASSWORD"));
            config.setMaximumPoolSize(10);

            HikariDataSource ds = new HikariDataSource(config);
            jdbi = Jdbi.create(ds);
        }
        return jdbi;
    }
}
```

Variáveis de ambiente:

```
DB_URL=jdbc:postgresql://localhost:5432/punish
DB_USER=postgres
DB_PASSWORD=postgres
```

### Schema SQL

O schema de referência está em `schema.sql` na raiz do projeto. Não usa Flyway — roda manualmente ou via script. [Ver schema completo](./schema.sql)

---

## 📂 Repository

Cada repositório segue o mesmo padrão JDBI da Weiva:

- Instância de `Jdbi` via `Database.getJdbi()`
- `mapToBean(Model.class)` para mapear linhas pra objeto
- `findOne()` + `orElse(null)` pra retorno opcional
- Parâmetros via `.bind("nome", valor)`

### TournamentRepository.java

| Método | Descrição |
|--------|-----------|
| `Tournament criar(String nome, String game)` | INSERT e retorna o criado |
| `Tournament buscarPorId(long id)` | SELECT por PK |
| `List<Tournament> buscarTodos()` | SELECT * |
| `Tournament atualizar(long id, String nome, String game)` | UPDATE |
| `void atualizarStatus(long id, String status)` | UPDATE status |
| `void deletar(long id)` | DELETE |

### PlayerRepository.java

| Método | Descrição |
|--------|-----------|
| `Player criar(String nickname)` | INSERT e retorna |
| `Player buscarPorId(long id)` | SELECT por PK |
| `Player buscarPorNickname(String nickname)` | SELECT por nickname |
| `List<Player> buscarTodos()` | SELECT * |

### TournamentPlayerRepository.java

| Método | Descrição |
|--------|-----------|
| `TournamentPlayer criar(long tournamentId, long playerId)` | INSERT |
| `void deletar(long tournamentId, long playerId)` | DELETE |
| `List<Player> buscarPlayersPorTournament(long tournamentId)` | JOIN player + tournament_player |
| `boolean existe(long tournamentId, long playerId)` | COUNT > 0 |
| `int contarPorTournament(long tournamentId)` | COUNT |
| `void atualizarSeed(long tournamentId, long playerId, int seed)` | UPDATE seed |
| `List<TournamentPlayer> buscarPorTournament(long tournamentId)` | SELECT filtrado |

### MatchRepository.java

| Método | Descrição |
|--------|-----------|
| `Match criar(Match match)` | INSERT |
| `Match buscarPorId(long id)` | SELECT por PK |
| `List<Match> buscarPorTournament(long tournamentId)` | SELECT por torneio |
| `List<Match> buscarPorTournamentEBracket(long tournamentId, String bracketType)` | SELECT filtrado |
| `void atualizarVencedor(long matchId, Long winnerId, Integer score1, Integer score2)` | UPDATE winner/scores/status |
| `void atualizarPlayer(long matchId, int slot, Long playerId)` | UPDATE player1_id ou player2_id |

---

## 🧠 Service

### TournamentService.java

| Método | Descrição |
|--------|-----------|
| `Tournament criar(String nome, String game)` | Validar, criar, retornar |
| `List<Tournament> buscarTodos()` | Delegar pro repository |
| `Tournament buscarPorId(long id)` | Lançar RuntimeException se não existir |
| `Tournament atualizar(long id, String nome, String game)` | Só se status != FINISHED |
| `void finalizar(long id)` | Só se status == STARTED |

### PlayerService.java

| Método | Descrição |
|--------|-----------|
| `Player criar(String nickname)` | Verificar se já existe, criar |
| `List<Player> buscarTodos()` | Listar todos |
| `Player buscarPorId(long id)` | Buscar ou null |
| `void adicionarAoTournament(long tournamentId, long playerId)` | Validar torneio, jogador, limite 16 |
| `void removerDoTournament(long tournamentId, long playerId)` | Só se CREATED |
| `List<Player> buscarPlayersDoTournament(long tournamentId)` | Listar participantes |

### MatchService.java

| Método | Descrição |
|--------|-----------|
| `List<Match> buscarPorTournament(long tournamentId)` | Listar partidas |
| `Match registrarResultado(long matchId, Long winnerId, Integer score1, Integer score2)` | Validar, atualizar, avançar bracket |
| `Match buscarPorId(long id)` | Buscar individual |

### BracketService.java ⚠️ (parte mais difícil)

#### Gerar bracket

```
Recebe lista de jogadores com seed
  ↓
Calcula próxima potência de 2 (8, 16)
  ↓
Cria byes se necessário
  ↓
Gera Winners Bracket:
  - Primeira rodada: seeds pareados
  - Rodadas seguintes: vencedores avançam
  - Conecta fk_next_match_win_id
  - Conecta fk_next_match_lose_id → Losers
  ↓
Gera Losers Bracket:
  - Perdedores da R1 de Winners vão pra Losers
  - Cada rodada de Losers tem metade dos jogadores
  ↓
Gera Grand Final:
  - Winner da última Winners vs Winner da última Losers
  - next_match_lose aponta pra Grand Final Reset
```

#### Avanço automático

```
registrarResultado(matchId)
  ↓
MatchService registra vencedor
  ↓
BracketService.avancarVencedor(match)
  → coloca winner_id na fk_player1_id ou fk_player2_id da next_match_win
  ↓
BracketService.avancarPerdedor(match)
  → coloca perdedor na next_match_lose (Losers)
  ↓
Se for Grand Final e veio da Losers:
  → criar Grand Final Reset
  → quem vencer o reset é campeão
```

#### Métodos

| Método | Descrição |
|--------|-----------|
| `List<Match> gerarBracket(long tournamentId)` | Gera chaveamento completo |
| `void avancarVencedor(Match partida)` | Avança winner pra próxima |
| `void avancarPerdedor(Match partida)` | Joga loser pra Losers |
| `void processarResultado(Match partida)` | Avança ambos + verifica reset |
| `List<Ranking> gerarRanking(long tournamentId)` | Ordena por colocação |

---

## 🎮 Controller

Cada controller segue o padrão Weiva: método público que recebe `Javalin app` e registra as rotas.

### TournamentController.java

```java
public void tournamentRoutes(Javalin app) {
    app.post("/tournaments", ctx -> { ... });
    app.get("/tournaments", ctx -> { ... });
    app.get("/tournaments/{id}", ctx -> { ... });
    app.put("/tournaments/{id}", ctx -> { ... });
    app.post("/tournaments/{id}/finish", ctx -> { ... });
    app.post("/tournaments/{id}/players", ctx -> { ... });
    app.get("/tournaments/{id}/players", ctx -> { ... });
    app.delete("/tournaments/{id}/players/{playerId}", ctx -> { ... });
}
```

### PlayerController.java

```java
public void playerRoutes(Javalin app) {
    app.post("/players", ctx -> { ... });
    app.get("/players", ctx -> { ... });
    app.get("/players/{id}", ctx -> { ... });
}
```

### MatchController.java

```java
public void matchRoutes(Javalin app) {
    app.post("/tournaments/{id}/generate", ctx -> { ... });
    app.get("/tournaments/{id}/matches", ctx -> { ... });
    app.get("/matches/{id}", ctx -> { ... });
    app.patch("/matches/{id}/result", ctx -> { ... });
    app.get("/tournaments/{id}/ranking", ctx -> { ... });
}
```

---

## 🚀 Main.java

```java
package com.punish;

import com.punish.Controller.*;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> rule.anyHost());
            });
        }).start(7000);

        new TournamentController().tournamentRoutes(app);
        new PlayerController().playerRoutes(app);
        new MatchController().matchRoutes(app);
    }
}
```

---

## 📊 Tabela de Prioridade

```
Fácil + Essencial → FAZ PRIMEIRO (P0)
Difícil + Essencial → FAZ DEPOIS (P1)
Fácil + Não essencial → FAZ NO FINAL (P2)
Difícil + Não essencial → ADIA (P3)
```

### P0 — Fundação

| # | O que | Esforço |
|---|-------|---------|
| 1 | `pom.xml` + estrutura de diretórios | ⚡ 10 min |
| 2 | **Enums** (TournamentStatus, BracketType, MatchStatus) | ⚡ 5 min |
| 3 | **Models** (Player, Tournament, TournamentPlayer, Match) | ⚡ 20 min |
| 4 | **BracketService** com `HashMap`/`List` (in-memory) | ⏳ 2-3h |
| 5 | **Testes do BracketService** (JUnit + AssertJ) | ⏳ 1h |

### P1 — API

| # | O que | Esforço |
|---|-------|---------|
| 6 | **Database.java** + `schema.sql` | ⚡ 20 min |
| 7 | **Repositories** (Tournament, Player, TournamentPlayer, Match) | ⏳ 1h |
| 8 | **TournamentController + TournamentService** | ⚡ 30 min |
| 9 | **PlayerController + PlayerService** | ⚡ 30 min |
| 10 | **MatchController + MatchService** | ⏳ 30 min |

### P2 — Sobrevivência

| # | O que | Esforço |
|---|-------|---------|
| 11 | Validações de input | ⚡ 20 min |
| 12 | Tratamento de erros (RuntimeException) | ⚡ 10 min |
| 13 | CORS config | ⚡ 5 min |
| 14 | Ranking endpoint | ⏳ 20 min |

### P3 — Futuro

| # | O que | Esforço |
|---|-------|---------|
| 15 | Auth (JWT) + User | ⏳ 3-4h |
| 16 | AuthMiddleware + AuthController | ⏳ 2h |
| 17 | Frontend | ⏳ semanas |
| 18 | Websocket | ⏳ 2-3h |

---

## 🧠 Estratégia: Bracket primeiro, API depois

```
1. Models + Enums (20 min)
2. BracketService com HashMap (2-3h) ← VALIDA A LÓGICA
3. Testes (1h) ← GARANTE QUE FUNCIONA
4. Só então: banco + API + controllers (3-4h)
```

---

## 📜 Regras de Negócio

- Não pode adicionar jogador a um torneio já iniciado
- Não pode gerar bracket com menos de 2 jogadores
- Não pode gerar bracket duas vezes
- Só pode registrar resultado de partida `READY` ou `IN_PROGRESS`
- `winnerId` precisa ser player1 ou player2
- Byes são resolvidos automaticamente na geração
- Grand Final Reset só acontece se o jogador da Losers vencer
- Torneio `FINISHED` não pode ser alterado
- Jogador não pode estar duplicado no mesmo torneio
- Máximo 16 jogadores no MVP

