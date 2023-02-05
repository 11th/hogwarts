select s.name, s.age, f.name
    from student as s
    left join faculty as f
        on s.faculty_id = f.id;

select s.name
    from avatar as a
    inner join student as s
        on s.id = a.student_id = s.id;