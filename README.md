## JAVA Guzi Simulator

This JAVA program is simulation about guzi economical behaviour in a two dimension world. This is based on a multi-agent system. Every day of the simulation, every agent (ie. guzi user) acts according his own rules and interacts with his cloth environment (ie. other guzi users).

### Examples
First simple example with only one guzi in 1*1 grid world

```java

// init the guzi world on a 1*1 grid
final GuziWorld world = new GuziWorld(1, 1);
    
// init a simple guzi user with no probability of buying or selling
new HumanGuziUser(world, world.getRandomFreePosition(), 0, 0);

// create the simulator to run this world
final MultiAgentSystem simulator = new MultiAgentSystem(world);

// run the simulator for 10000 days with 500 milliseconds minimum between each day
simulator.playsRounds(10000, 500);


```



Second example with 100 guzis on 20*20 grid with random probabilty of buying or selling something

```java

// init the guzi world on a 20*20 grid
final GuziWorld world = new GuziWorld(20, 20);

// create 100 random users
int cptNbGuziUsersCreated = 0;
while (cptNbGuziUsersCreated++ < 100) {
  try {
    final Random rand = new Random();
    final Integer sellingIntentionProbability = rand.nextInt(100);
    final Integer buyingIntentionProbability = rand.nextInt(100);
    new HumanGuziUser(world, world.getRandomFreePosition(), sellingIntentionProbability, buyingIntentionProbability);
  }
  catch (final NoFreePositionException e) {
    // 20 * 20 = 400 is normally enough to contain every user so we normally don't go there
    break;
  }
}

// create the simulator to run this world
final MultiAgentSystem simulator = new MultiAgentSystem(world);

// run the simulator for 10000 days with 500 milliseconds minimum between each day
simulator.playsRounds(10000, 500);


```



We can custom the simulator days running like this :



```java

...

// play one day
simulator.playRound();

// play a week with no latency
simulator.playRounds(7)

// play a year with 10 milliseconds minimum between each day
simulator.playRounds(365, 10)

```