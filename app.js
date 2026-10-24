const roleSelect = document.getElementById('roleSelect');
const skillInput = document.getElementById('skillInput');
const chips = document.getElementById('skillChips');
const message = document.getElementById('message');
const skills = [];

function showMessage(text='') { message.textContent = text; }
function renderSkills() {
  chips.innerHTML = '';
  if (!skills.length) {
    chips.innerHTML = '<span class="empty">No skills added yet.</span>';
    return;
  }
  skills.forEach((skill, index) => {
    const chip = document.createElement('span');
    chip.className = 'chip';
    const label = document.createElement('span');
    label.textContent = skill;
    const remove = document.createElement('button');
    remove.type = 'button'; remove.textContent = '×'; remove.setAttribute('aria-label', `Remove ${skill}`);
    remove.onclick = () => { skills.splice(index, 1); renderSkills(); };
    chip.append(label, remove); chips.appendChild(chip);
  });
}
function addSkill() {
  const skill = skillInput.value.trim();
  if (!skill) return showMessage('Enter a skill first.');
  if (skills.some(s => s.toLowerCase() === skill.toLowerCase())) return showMessage('That skill is already in your list.');
  skills.push(skill); skillInput.value = ''; showMessage(''); renderSkills(); skillInput.focus();
}
document.getElementById('addSkill').addEventListener('click', addSkill);
skillInput.addEventListener('keydown', e => { if (e.key === 'Enter') addSkill(); });

async function loadRoles() {
  try {
    const response = await fetch('/api/roles');
    if (!response.ok) throw new Error('Could not load roles.');
    const roles = await response.json();
    roleSelect.innerHTML = '<option value="">Select a target role</option>';
    roles.forEach(role => {
      const option = document.createElement('option');
      option.value = role.id; option.textContent = role.title; roleSelect.appendChild(option);
    });
  } catch (e) {
    roleSelect.innerHTML = '<option value="">Backend not available</option>';
    showMessage('Start the Spring Boot application and check your MySQL settings.');
  }
}
function fillList(id, values, emptyText) {
  const list = document.getElementById(id); list.innerHTML = '';
  const items = values.length ? values : [emptyText];
  items.forEach(value => { const li = document.createElement('li'); li.textContent = value; list.appendChild(li); });
}
document.getElementById('analyzeBtn').addEventListener('click', async () => {
  showMessage('');
  if (!roleSelect.value) return showMessage('Please choose a target role.');
  if (!skills.length) return showMessage('Add at least one skill to analyze.');
  const button = document.getElementById('analyzeBtn');
  button.disabled = true; button.firstChild.textContent = 'Analyzing... ';
  try {
    const response = await fetch('/api/analyze', {
      method: 'POST', headers: {'Content-Type':'application/json'},
      body: JSON.stringify({roleId: Number(roleSelect.value), skills})
    });
    const data = await response.json();
    if (!response.ok) throw new Error(data.message || 'Analysis failed. Please try again.');
    document.getElementById('resultRole').textContent = data.roleTitle;
    document.getElementById('score').textContent = data.readinessPercent + '%';
    document.getElementById('progressFill').style.width = data.readinessPercent + '%';
    fillList('matchedList', data.matchedSkills, 'No required skills matched yet');
    fillList('missingList', data.missingSkills, 'You have all listed skills!');
    document.getElementById('recommendation').textContent = data.recommendation;
    document.getElementById('results').classList.remove('hidden');
    document.getElementById('results').scrollIntoView({behavior:'smooth', block:'start'});
  } catch (e) { showMessage(e.message); }
  finally { button.disabled = false; button.firstChild.textContent = 'Analyze my skill gap '; }
});
loadRoles();
