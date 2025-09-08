# Path-finding-algorithms
Includes demonstration of Dijkstra's algorithm. Also includes a reinforcement learning algorithm made from scratch in java to solve a 2D grid maze using q-learning. Generates mazes using prim's algorithm.
All coded in Java with no libraries.

## Prim's algorithm
* Generates a random 2D grid maze using prim's algorithm
* Builds a maze by starting from a random cell
* Repeatedly adds the lowest-cost wall that connects the growing maze to a new unvisited cell until all cells are included.

## Dijkstra's algorithm

* Applies Dijkstra's algorithm to a 2D grid maze
* Finds the shortest path from a starting node to all other nodes by repeatedly selecting the unvisited node with the smallest known distance
* Updates its neighbors’ distances until all nodes are processed.
* Works back from target node and selects the adjacent node with smallest distance from start until start is reached

## Reinforcement Learning algorithm

* Assigns a q-value to each action in each state
* Uses epsilon-greedy algorithm to initially priortise exploration and eventually decays to prioritse exploitation
* Runs episodes and gives agent reward based on how close agent got to target node and punishes revisiting tiles multiple times
* Saves episodes and replays them to update q-values

## Limitations

* No use of neural networks for deep q-learning
