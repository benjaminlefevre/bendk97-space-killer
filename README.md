# SPACE KILLER [![Codacy Badge](https://api.codacy.com/project/badge/Grade/c7642cf8784d4cb2b7c153482e924d80)](https://app.codacy.com/app/benjamin.lefevre.github/space-killer?utm_source=github.com&utm_medium=referral&utm_content=benjaminlefevre/space-killer&utm_campaign=Badge_Grade_Dashboard) [![Build Status](https://travis-ci.org/benjaminlefevre/space-killer.svg?branch=master)](https://travis-ci.org/benjaminlefevre/space-killer) [![Coverage Status](https://coveralls.io/repos/github/benjaminlefevre/space-killer/badge.svg?branch=master&service=github)](https://coveralls.io/github/benjaminlefevre/space-killer?branch=master) [![PyPI license](https://img.shields.io/pypi/l/ansicolortags.svg)](LICENSE)

![alt logo](https://lh3.googleusercontent.com/_Oum51HCcglCmq1Y8qDrqSOT5ne2dnq28ZcJN4HG5MLE2ORDeF27ypSZHNohbHSzyA=s180-rw)

Space Killer is a shmup game (shoot'em up) developed with libgdx, a cross-platform java game development.
The game is currently published in the google playstore here: [Space Killer](https://play.google.com/store/apps/details?id=com.benk97.space.killer&hl=en_US)

Several libraries are used:

| Library                     | Version | Link                                                                                     |
| --------------------------- | ------- | ---------------------------------------------------------------------------------------- |
| libgdx                      | 1.9.8   | [Libgdx](https://libgdx.badlogicgames.com/) - [GitHub](https://github.com/libgdx/libgdx) |
| ashley (ECS design pattern) | 1.7.3   | [Ashley](https://github.com/libgdx/ashley)                                               |
| box2DLights                 | 1.4     | [Box2DLights](https://github.com/libgdx/box2dlights)                                     |
| Tween Universal Engine      | 6.3.3   | [Tween](https://github.com/AurelienRibon/universal-tween-engine)                         |
| gdx-postprocessing          | 1.0.8   | [gdx-postprocessing](https://github.com/Anuken/gdx-postprocessing)                       |

The game is only tested and compiled for android platforms :
```bash
gradle android:assembleRelease
```
But as libgdx is cross-platform, it should be easy to compile for HTML5, iOS...and so on

## Dependency graph

Generated with this [gradle plugin](https://github.com/vanniktech/gradle-dependency-graph-generator-plugin)

![alt dependencies](dependency-graph-main-libraries.svg)

## LICENSE

This project is licenced under the MIT license.

For more details check the [LICENCE](LICENSE) file

## CI
Travis CI is installed to build every Pull Requests and the master on a daily basis.
