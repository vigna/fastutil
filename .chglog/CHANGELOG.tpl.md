# {{ .Info.Title }}

This changelog records changes made in the Luxonit fork of fastutil.
It is maintained as a prominent notice of modifications for this Apache License 2.0 distribution.
Upstream project: <https://github.com/vigna/fastutil>

{{ range .Versions }}

## {{ if .Tag.Previous }}[{{ .Tag.Name }}]({{ $.Info.RepositoryURL }}/compare/{{ .Tag.Previous.Name }}...{{ .Tag.Name }}){{ else }}{{ .Tag.Name }}{{ end }}

> {{ datetime "2006-01-02" .Tag.Date }}

{{ range .CommitGroups }}

### {{ .Title }}

{{ range .Commits }}

- {{ .Subject }}

{{ end }}
{{ end }}

{{ if .RevertCommits }}

### Reverts

{{ range .RevertCommits }}

- {{ .Revert.Header }}

{{ end }}
{{ end }}

{{ if .MergeCommits }}

### Pull Requests

The merged pull requests below identify the modifications included in this version.

{{ range .MergeCommits }}

- {{ .Header }}

{{ end }}
{{ end }}

{{ if .NoteGroups }}
{{ range .NoteGroups }}

### Notes: {{ .Title }}

{{ range .Notes }}
{{ .Body }}
{{ end }}
{{ end }}
{{ end }}
{{ end }}
