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
let currentUser = {
    user: {},
    updateCurrentUser: async () => {
        currentUser.user = await fetch('/api/user')
            .then(response => response.json())
    },
    getRoleListFromCurrentUser: () => {
        let roles = []
            currentUser.user.roles.forEach(role => roles.push(role.name.slice(5)))
        return roles
    }

}

let allRoles = {
    list: {},
    update: async () => {
        allRoles.list = await fetch('/api/admin/roles')
            .then(response => response.json())
    },


}

// Объект со всеми пользователями
let users = {
    userMap: new Map(),
    update: async () => {
        let userList = await fetch('/api/admin/users').then(response => response.json())
        userList.forEach(user => users.userMap.set(user.id, user))
        await updateUserTable()
    },
    remove: async (id) => {
        users.userMap.delete(id)
        await updateUserTable()
    },
    save: async (user) => {
        users.userMap.set(user.id, user)
        await updateUserTable()
    }
}


$(document).ready(async function () {

    await currentUser.updateCurrentUser()
   let x = currentUser.user
   let y = currentUser.getRoleListFromCurrentUser()

    console.log(x)
    console.log(y)
    console.log(y[0])
});




