# Contributing  to the Project

### Table of Contents

* [Branching Aspects](#Branching Aspects)
* [Issue Lifecycle](#issue-lifecycle)
  [Submit a Pull Request](#submit-a-pull-request)
* [Releases](#releases)
* [Reference Docs](#reference-docs)

### How to Contribute

#### Branching Aspects

Naming convention below should be respected during the whole project:

|        | Naming Conventions               |
|--------|----------------------------------|
| Branch | Feature- [FeatureName]-#N°Issues |
| Commit | [Content]-#N°Issues              |

A Branch shall be created each a feature is developed, and will be merged into master when the feature is fully
functional, with tests. After the merge, the branch will be deleted and the linked issue closed.

#### Issue Lifecycle

An issue shall be created for each feature.

When an issue is first created, based on comments, the issue is assigned to a target milestone and to a person

When the feature is ready, with tests, Javadocs and comments if needed, the issue is closed . After that the issue will
typically no longer be reopened. In rare cases if the issue was not at all fixed, the issue may be re-opened. In most
cases however any follow-up reports will need to be created as new issues with a fresh description.

#### Submit a Pull Request

Pull request will be submitted only when importants side effects could happen. In those cases, at least one person
should give his approval in order to merge the feature into the master branch

#### Releases

A release by week is expected, with the tag "WEEKX", where X is the number of the current week. Those releases should be
stables. If not, hotfixes can be made by cherry-picking from an existing branch in order to solve the bug.

### Reference Docs

The reference documentation is in the [QGL template](https://github.com/mathiascouste/qgl-2122/tree/master/project), in
Markdown format. For more details, you can check questions and answers, or ask one of your own on the
slack [si3-qgl-21-22](https://informatiquep-kyw7477.slack.com/archives/C02SM03CCAW)




