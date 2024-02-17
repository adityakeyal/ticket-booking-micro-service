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
    
    
</div>