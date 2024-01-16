This Spring project aims to calculate some basic statistics on GitHub repositories.

First, you have to create a GitHub Token. You can create one <a href="https://github.com/settings/tokens" target="_blank">HERE</a>. (If the link does not work, please search to find out how to create a github personal access token.)

After creating an access token, please modify the 'token' in the following section in ```application.yml``` file :

```yaml
github:
  base_address: "https://api.github.com/users/{USER_NAME}/repos?per_page=100&page={I}"
  deployment_address: "https://api.github.com/repos/{USER_NAME}/{REPO_NAME}/deployments"
  environment_address: "https://api.github.com/repos/{USER_NAME}/{REPO_NAME}/environments"
  issues_address: "https://api.github.com/repos/{USER_NAME}/{REPO_NAME}/issues?state=closed&per_page=1"
  token: "Bearer {PUT YOUR PERSONAL ACCESS TOEKN HERE.}"
```

It should be something like 
```yaml
  token: "Bearer ghp_h***9"
```

Then, start the Java app. It will start a tomcat server on port 8080.

You can use the following URL to ask for statistics :

```
http://localhost:8080/repos/{REPO_NAME}
```

For example, to have a look at Kaggle repositories, paste the following URL in your browser's search bar:

```
http://localhost:8080/repos/kaggle
```


The server will automatically start on port 8080. If you want to start it on another port, just change the following code in ```src/main/resources/application.yml``` file :

``` yaml
server:
  port: 8080
```


Here is the result from querying Kaggle repositories :


```json
{
    "repositoryCounts": 11,
    "commitsCount": 6574,
    "commitsMedian": 153,
    "commits": [1, 2, 36, 58, 84, 153, 420, 438, 455, 2309, 2618],
    "starsCount": 8973,
    "starsMedian": 42,
    "stars": [1, 5, 7, 18, 36, 42, 138, 265, 424, 2298, 5739],
    "contributorsCount": 259,
    "contributorsMedian": 8,
    "contributors": [0, 0, 4, 5, 6, 8, 17, 27, 29, 33, 130],
    "branchCount": 150,
    "branchMedian": 6,
    "branches": [0, 0, 0, 0, 3, 6, 6, 10, 26, 32, 67],
    "tagsCount": 318,
    "tagsMedian": 2,
    "tags": [0, 0, 0, 0, 0, 2, 5, 8, 19, 91, 193],
    "forksCount": 2504,
    "forksMedian": 14,
    "forks": [0, 1, 6, 10, 13, 14, 90, 138, 227, 955, 1050],
    "releasesCount": 228,
    "releasesMedian": 0,
    "releases": [0, 0, 0, 0, 0, 0, 0, 5, 6, 72, 145],
    "closedIssuesCount": 2705,
    "closedIssuesMedian": 70,
    "closedIssues": [0, 0, 7, 9, 63, 70, 201, 227, 382, 433, 1313],
    "environmentsCount": 0,
    "environmentsMedian": 0,
    "environments": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    "deploymentsCount": 0,
    "deploymentsMedian": 0,
    "deployments": [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    "languages": {
        "Smarty": {
            "name": "Smarty",
            "total": 916,
            "median": 458
        },
        "Java": {
            "name": "Java",
            "total": 77923,
            "median": 77923
        },
        "CSS": {
            "name": "CSS",
            "total": 856,
            "median": 856
        },
        "HTML": {
            "name": "HTML",
            "total": 26101,
            "median": 26101
        },
        "Jupyter Notebook": {
            "name": "Jupyter Notebook",
            "total": 4783676,
            "median": 1847244
        },
        "PureBasic": {
            "name": "PureBasic",
            "total": 4637,
            "median": 4637
        },
        "TypeScript": {
            "name": "TypeScript",
            "total": 123163,
            "median": 84404
        },
        "Julia": {
            "name": "Julia",
            "total": 1364,
            "median": 1364
        },
        "Dockerfile": {
            "name": "Dockerfile",
            "total": 28380,
            "median": 2082
        },
        "R": {
            "name": "R",
            "total": 29621,
            "median": 5769
        },
        "Shell": {
            "name": "Shell",
            "total": 92705,
            "median": 16155
        },
        "JavaScript": {
            "name": "JavaScript",
            "total": 128405,
            "median": 128302
        },
        "Python": {
            "name": "Python",
            "total": 2240116,
            "median": 548414
        }
    }
}
```
