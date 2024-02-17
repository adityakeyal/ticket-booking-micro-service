<div class="mermaid">  
erDiagram
    USER {
        uuid id
        varchar name
        varchar email
        varchar phone
        varchar role
        varchar theater_owner_id
    }
    ROLES {
        uuid id
        varchar type
        varchar role_name
    }
    USER_ROLE {
        uuid id
        uuid user_id
        uuid role_id
    }
    ADDRESS {}

    USER ||--o{ USER_ROLE : has
    USER_ROLE }o--||ROLES: has
    USER ||--o{ ADDRESS : has
    
     BOOKING {
        id uuid
        uuid show_id
        uuid user_id
        date show_date
        varchar payment_status
        uuid payment_id
        varchar status
    }
    BOOKING_DETAILS {
        id uuid
        uuid booking_id
        uuid seat_id

    }
     SEAT_INVENTORY {
        uuid id
        uuid show_id
        real price
        int row
        varchar seat
        varchar availability 
    }
    
    BOOKING ||--o{ BOOKING_DETAILS : contains
    SEAT_INVENTORY ||--|| BOOKING_DETAILS: mapped

     MOVIE {
        uuid id
        varchar name
        varchar language
        varchar genre
        varchar release_date
        int running_time_min

    }
    MOVIE_CAST_CREW {
        uuid id
        uuid movie_id
        varchar name
        varchar imageloc
        varchar role 
    }
    MOVIE_IMAGES {
        uuid id
        uuid movie_id
        varchar image_location
    }
    MOVIE_VIDEOS {
        uuid id
        uuid movie_id
        varchar video_location
    }
    MOVIE }o--o{ MOVIE_CAST_CREW : involved
    MOVIE ||--o{ MOVIE_IMAGES : has 
    MOVIE ||--o{ MOVIE_VIDEOS : has


 DISCOUNT {
        uuid id
        show_id
        theater_id
        varchar pricing_strategy
        date start_date
        date end_date
    }


     THEATER_OWNER {
        uuid id
    }
    THEATER {
        uuid id
        string name
        string city
        string country
        point location
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