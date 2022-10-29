// Текущий пользователь
// Все пользователи
// Все роли


//Получение пользователя по id
//
// удаление пользователя
// редактирование пользователя
// создание пользователя
//
// удаление admin panel если user
// обновление панели
// обновление текущего юзера
//

$(document).ready(async function () {
    await currentUser.updateCurrentUserInfo()
    await showMainPageByRole()
    await openDeleteForm()
    await openEditForm()
});

let currentUser = {
    user: {},
    updateCurrentUserInfo: async () => {
        currentUser.user = await fetch('/api/user')
            .then(response => response.json())
        await updateHeader()
        await updateCurrentUserTable()
    },
    hasRole: (name) => {
        let result = false
        currentUser.user.roles.forEach(role => {
            if (role.name === name) {
                result = true
            }
        })
        return result
    },
    getRoleLabelListFromCurrentUser: () => {
        let roles = []
        currentUser.user.roles.forEach(role => roles.push(role.name.slice(5)))
        return roles
    }

}

let allRoles = {
    list: {},
    getAllRoles: async () => {
        allRoles.list = await fetch('/api/admin/roles')
            .then(response => response.json())
    },
}

// Объект со всеми пользователями
let users = {
    userMap: new Map(),
    updateAllUsersInfo: async () => {
        let userList = await fetch('/api/admin/users').then(response => response.json())
        userList.forEach(user => users.userMap.set(user.id, user))
        await updateAllUsersPanel()
    },
    removeUserById: async (id) => {
        users.userMap.delete(id)
        await updateAllUsersPanel()
    },
    addUser: async (user) => {
        users.userMap.set(user.id, user)
        await updateAllUsersPanel()
    }
}

async function updateHeader() {
    $('#currentEmail').text(currentUser.user.email)
    $('#currentUserRoles').text(currentUser.getRoleLabelListFromCurrentUser().join(" "));
}

async function updateCurrentUserTable() {
    let row = $('#userInfoTable tbody tr:first')
    row.find('td').eq(0).text(currentUser.user.id)
    row.find('td').eq(1).text(currentUser.user.name)
    row.find('td').eq(2).text(currentUser.user.lastname)
    row.find('td').eq(3).text(currentUser.user.age)
    row.find('td').eq(4).text(currentUser.user.email)
    row.find('td').eq(5).text(currentUser.getRoleLabelListFromCurrentUser().join(" "))
}

async function updateAllUsersPanel() {
    let table = $("#allUsersTable tbody")
    table.html("")
    users.userMap.forEach(user => {
        let button_edit = '<button class="btn btn-info user-edit-button" data-toggle="modal" data-target="#userEditModal" data-user-id="' + user.id + '">Edit</button> '
        let button_delete = '<button class="btn btn-danger user-delete-button" data-toggle="modal" data-target="#userDeleteModal" data-user-id="' + user.id + '">Delete</button>'
        let roles = []
        user.roles.forEach(role => roles.push(role.name.slice(5)))
        let row = "<tr>" +
            "<td>" + user.id + "</td>" +
            "<td>" + user.name + "</td>" +
            "<td>" + user.lastname + "</td>" +
            "<td>" + user.age + "</td>" +
            "<td>" + user.email + "</td>" +
            "<td>" + roles.join(" ") + "</td>" +
            "<td>" + button_edit + " " + button_delete + "</td>" +
            "</tr>"

        table.append(row)
    })
}

async function showMainPageByRole() {
    let page = location.pathname.substring(1)
    $('#v-pills-' + page + '-tab').tab('show')
    if (currentUser.hasRole("ROLE_ADMIN")) {
        await users.updateAllUsersInfo()
        await allRoles.getAllRoles()
    } else {
        document.body.querySelector("#v-pills-admin-tab").remove();
        document.body.querySelector("#v-pills-user-tab").setAttribute("class", "nav-link active");
        document.body.querySelector("#v-pills-user-tab").setAttribute("aria-selected", "true")
    }
}

async function openDeleteForm() {
    await $('#allUsersTable').on('click', '.user-delete-button', function () {
        let userId = Number($(this).attr('data-user-id'))
        let deletingUserInfo = users.userMap.get(userId)

        $('#delete_id').val(deletingUserInfo.id)
        $('#delete_name').val(deletingUserInfo.name)
        $('#delete_lastname').val(deletingUserInfo.lastname)
        $('#delete_age').val(deletingUserInfo.age)
        $('#delete_Email').val(deletingUserInfo.email)
        let domRoles = $('#delete_roles').empty();
        deletingUserInfo.roles.forEach(role => domRoles.append('<option value="' + role.name + '">' + role.name.slice(5)))
    });
    await deleteUser()
}

async function deleteUser() {
    $('#userDeleteForm').on("submit", async function (event) {
        event.preventDefault();
        let userId = Number($(this).find('#delete_id').val())

        let response = await fetch('/api/admin/users/' + userId, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        if (response.status === 200) {
            await users.removeUserById(userId)
            $('#userDeleteModal').modal('hide');
        } else {
            console.log(response.status)
        }
    })
}

async function openEditForm() {
    await $('#allUsersTable').on('click', '.user-edit-button', function () {
        let userId = Number($(this).attr('data-user-id'))
        let editingUserInfo = users.userMap.get(userId)

        $('#edit_id').val(editingUserInfo.id)
        $('#edit_name').val(editingUserInfo.name)
        $('#edit_lastname').val(editingUserInfo.lastname)
        $('#edit_age').val(editingUserInfo.age)
        $('#edit_Email').val(editingUserInfo.email)
        $('#edit_password').val(editingUserInfo.password)
        let domRoles = $('#edit_roles').empty();
        allRoles.list.forEach(role => domRoles.append('<option value="' + role.name + '">' + role.name.slice(5)))
    });
    await editUser()
}

async function editUser() {

    $('#userEditForm').on("submit", async function (event) {
        event.preventDefault(); // return false
        let userId = Number($(this).find('#edit_id').val())
        //
       // console.log(new FormData(event.currentTarget).forEach((value, key) => console.log("key " + key + " value " + value)))
        let user = getJsonFromUserForm(event.currentTarget)
        console.log(user)
        let response = await fetch('/api/admin/users/' + userId, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user)
        });
        console.log(response)
        if (response.status === 200) {
            console.log(response)
            await users.addUser(await response.json())
            $('#userEditModal').modal('hide');
            this.reset()
        }
    })

}

function getJsonFromUserForm(form) {
    let formData = new FormData(form);
    let user = {
        id: formData.get('id'),
            name : formData.get('name'),
        lastname : formData.get('lastname'),
        age: formData.get('age'),
            email: formData.get('email'),
        password : formData.get('password'),
        roles : formData.getAll("roles")

        }

    return user;
 }
