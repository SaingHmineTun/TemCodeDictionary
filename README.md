# Burma Dictionary

## Overview

Burma Dictionary is a desktop application developed in JavaFX that provides a comprehensive Myanmar language dictionary. This application allows users to search, browse, and listen to pronunciations of Myanmar words with an elegant and modern user interface.

The dictionary data is sourced from [Ornagai-v3](https://github.com/saturngod/ornagai-v3/) which is available under the MIT License.

![Burma Dictionary Interface](https://github.com/user-attachments/assets/08625246-f619-48ec-bbb5-2df06788c950)

## Features

- **Modern UI Design**: Enhanced with gradient backgrounds and a visually appealing interface
- **Comprehensive Search**: Quickly search through thousands of Myanmar words
- **Word Pronunciation**: Text-to-speech functionality for word pronunciation
- **Synonym Display**: View related words and synonyms for better understanding
- **Cross-Platform**: Runs on any desktop with JDK_FX installed

## Developer Information

### Sai Mao (TMK)

A passionate developer with expertise in Java and desktop application development. Focused on creating user-friendly applications with modern UI designs that enhance user experience.

## Installation

### Prerequisites

The application requires JDK_FX (JDK with JavaFX) to run. If you don't have JDK installed or only have JDK without JavaFX, you can download JDK_FX from [Azul Downloads](https://www.azul.com/downloads/#zulu).

### Setup

On first run, the application automatically creates a database file at:
```
/ProgramData/BurmaDictionary/mm_dictionary.db
```

If the database file doesn't exist, the application will create one for you from the embedded database.

## Usage

1. Launch the application
2. Use the search bar to find Myanmar words
3. Browse through the search results in the left panel
4. Select any word to view its definition, state, and synonyms in the right panel
5. Click the speaker icon to hear the pronunciation of the selected word

## Technology Stack

- **JavaFX**: For the rich user interface
- **SQLite**: Lightweight database for storing dictionary data
- **FreeTTS**: Text-to-speech engine for word pronunciation
- **Maven**: Build automation tool

## Contributing

Contributions are welcome! Feel free to fork the repository and submit pull requests for improvements, bug fixes, or new features.

## License

This project uses dictionary data from [Ornagai-v3](https://github.com/saturngod/ornagai-v3/) which is available under the MIT License.