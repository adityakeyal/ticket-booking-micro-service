<div class="mermaid">  
erDiagram
    THEATER_OWNER {
        uuid id
    }
    THEATER {
        uuid id
        string name
        string city
        string country
        uuid owner_id
    }
    SCREEN {
        uuid id
        uuid theater_id
        string scren_name
        string screen_type
        string total_seating
    }
    SHOWS {
        uuid id
        uuid screen_id
        date play_on
        time start_time 
        uuid movie_id
        string availability
    }
    THEATER_OWNER ||--o{ THEATER : owns
    THEATER ||--o{ SCREEN : contains
    SCREEN ||--o{ SHOWS : show
</div>