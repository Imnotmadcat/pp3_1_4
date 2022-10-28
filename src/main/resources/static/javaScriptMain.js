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







